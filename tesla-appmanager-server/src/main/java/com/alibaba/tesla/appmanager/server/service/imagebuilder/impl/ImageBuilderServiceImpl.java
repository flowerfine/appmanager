package com.alibaba.tesla.appmanager.server.service.imagebuilder.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.tesla.appmanager.autoconfig.ImageBuilderProperties;
import com.alibaba.tesla.appmanager.common.constants.RedisKeyConstant;
import com.alibaba.tesla.appmanager.common.exception.AppErrorCode;
import com.alibaba.tesla.appmanager.common.exception.AppException;
import com.alibaba.tesla.appmanager.common.service.GitService;
import com.alibaba.tesla.appmanager.common.service.StreamLogService;
import com.alibaba.tesla.appmanager.common.util.CommandUtil;
import com.alibaba.tesla.appmanager.common.util.ImageUtil;
import com.alibaba.tesla.appmanager.domain.req.git.GitCloneReq;
import com.alibaba.tesla.appmanager.domain.req.imagebuilder.ImageBuilderCreateReq;
import com.alibaba.tesla.appmanager.domain.res.imagebuilder.ImageBuilderCreateRes;
import com.alibaba.tesla.appmanager.server.service.imagebuilder.ImageBuilderService;
import com.google.common.collect.Lists;
import com.hubspot.jinjava.Jinjava;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeroturnaround.exec.stream.LogOutputStream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 镜像构建服务
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
@Service
@Slf4j
public class ImageBuilderServiceImpl implements ImageBuilderService {

    private static final String HTTP_PREFIX = "http://";
    private static final String HTTPS_PREFIX = "https://";

    @Autowired
    private ImageBuilderProperties imageBuilderProperties;

    @Autowired
    private GitService gitService;

    @Autowired
    private StreamLogService streamLogService;

    /**
     * 镜像构建
     *
     * @param request 请求配置
     * @return Future 返回结果
     */
    @Override
    public Future<ImageBuilderCreateRes> build(ImageBuilderCreateReq request) {
        // TODO: 恢复为正常 Future 形式
        return ConcurrentUtils.constantFuture(processor(request));
    }

    /**
     * 镜像构建过程
     *
     * @param request 请求配置
     * @return Future 返回结果
     */
    private ImageBuilderCreateRes processor(ImageBuilderCreateReq request) {
        String streamKey = String.format("%s_%s_%s_%s_%s", RedisKeyConstant.COMPONENT_PACKAGE_TASK_LOG,
                request.getAppId(), request.getComponentType(), request.getComponentName(), request.getVersion());
        StringBuilder logContent = new StringBuilder();
        Path cloneDir, imageDir;
        try {
            cloneDir = Files.createTempDirectory("appmanager_image_builder_clone_");
            imageDir = Files.createTempDirectory("appmanager_image_builder_image_");
            cloneDir.toFile().deleteOnExit();
            imageDir.toFile().deleteOnExit();
        } catch (IOException e) {
            throw new AppException(AppErrorCode.UNKNOWN_ERROR, "cannot create temp directory", e);
        }

        try {
            // 如果自行提供 image，直接返回
            if (StringUtils.isNotEmpty(request.getUseExistImage())) {
                String sha256 = getDockerImageSha256(logContent, request.getUseExistImage(), request.getArch(), streamKey);
                return ImageBuilderCreateRes.builder()
                        .imageName(request.getUseExistImage())
                        .sha256(sha256)
                        .logContent(logContent.toString())
                        .imageDir(imageDir.toString())
                        .imagePath("")
                        .build();
            }

            // 设置 image 名称
            String imageName = getImageName(request);

            // Clone 仓库
            gitService.cloneRepo(logContent, GitCloneReq.builder()
                    .repo(request.getRepo())
                    .branch(request.getBranch())
                    .commit(request.getCommit())
                    .repoPath(request.getRepoPath())
                    .ciAccount(request.getCiAccount())
                    .ciToken(request.getCiToken())
                    .keepGitFiles(request.isKeepGitFiles())
                    .build(), cloneDir);

            // 渲染 Dockerfile
            Path dockerfile;
            try {
                dockerfile = renderDockerfile(logContent, request, cloneDir);
            } catch (IOException e) {
                throw new AppException(AppErrorCode.INVALID_USER_ARGS, "cannot render dockerfile template", e);
            }

            // 构建镜像
            String imagePath = dockerBuild(logContent, request, cloneDir, imageDir, imageName, dockerfile, streamKey);
            try {
                FileUtils.deleteDirectory(cloneDir.toFile());
            } catch (Exception ignored) {
                log.warn("cannot delete directory after docker build|directory={}", cloneDir);
            }
            String sha256 = getDockerImageSha256(logContent, imageName, request.getArch(), streamKey);
            return ImageBuilderCreateRes.builder()
                    .imageName(imageName)
                    .sha256(sha256)
                    .logContent(logContent.toString())
                    .imageDir(imageDir.toString())
                    .imagePath(imagePath)
                    .build();
        } finally {
//            streamLogService.clean(streamKey,false);
        }
    }

    /**
     * 获取指定 imageName 的镜像 sha256 值
     *
     * @param logContent 日志内容
     * @param imageName  镜像名称
     * @param arch       Arch (x86/arm/sw6b) 可为空
     * @return sha256
     */
    private String getDockerImageSha256(StringBuilder logContent, String imageName, String arch, String streamKey) {
        String remoteDaemon = imageBuilderProperties.getRemoteDaemon();
        String armRemoteDaemon = imageBuilderProperties.getArmRemoteDaemon();
        String dockerBin = imageBuilderProperties.getDockerBin();
        boolean useSudo = imageBuilderProperties.isUseSudo();
        String dockerTarget = "";
        if (StringUtils.isNotEmpty(remoteDaemon)) {
            dockerTarget = String.format("-H %s", remoteDaemon);
        } else if (StringUtils.isNotEmpty(armRemoteDaemon) && "arm".equals(arch)) {
            dockerTarget = String.format("-H %s", armRemoteDaemon);
        }
        String sudoCommand = "";
        if (useSudo) {
            sudoCommand = "sudo";
        }

        // 拉取镜像 (abm.io 意味着是自己本地构建的镜像，不再需要拉取了)
        if (imageName.startsWith("abm.io")) {
            // 计算 SHA256
            String[] calcCommand = new String[]{sudoCommand, dockerBin, dockerTarget, "images", "--no-trunc", "--quiet", imageName};
            logContent.append(String.format("run command: %s\n", String.join(" ", calcCommand)));
            streamLogService.info(streamKey, String.format("run command: %s\n", String.join(" ", calcCommand)));
            String result = CommandUtil.runLocalCommand(CommandUtil.getBashCommand(calcCommand), new LogOutputStream() {
                @Override
                protected void processLine(String line) {
                    streamLogService.info(streamKey, line, log);
                }
            });
            logContent.append(result);

            // 从 fullname 中提取
            String fullname = result.trim();
            String split = "sha256:";
            return fullname.substring(split.length());
        } else {
            String[] pullCommand = new String[]{sudoCommand, dockerBin, dockerTarget, "pull", imageName};
            logContent.append(String.format("run command: %s\n", String.join(" ", pullCommand)));
            String result = CommandUtil.runLocalCommand(CommandUtil.getBashCommand(pullCommand), new LogOutputStream() {
                @Override
                protected void processLine(String line) {
                    streamLogService.info(streamKey, line, log);
                }
            });
            logContent.append(result);

            // 计算 SHA256
            String[] calcCommand = new String[]{sudoCommand, dockerBin, dockerTarget, "inspect", "--format='{{index .RepoDigests 0}}'", imageName};
            logContent.append(String.format("run command: %s\n", String.join(" ", calcCommand)));
            streamLogService.info(streamKey, String.format("run command: %s\n", String.join(" ", calcCommand)));
            result = CommandUtil.runLocalCommand(CommandUtil.getBashCommand(calcCommand), new LogOutputStream() {
                @Override
                protected void processLine(String line) {
                    streamLogService.info(streamKey, line, log);
                }
            });
            logContent.append(result);

            // 从 fullname 中提取
            String fullname = result.trim();
            String split = "sha256:";
            return fullname.substring(fullname.indexOf(split) + split.length());
        }
    }

    /**
     * 构建 Docker 镜像过程
     *
     * @param logContent 日志内容
     * @param request    镜像构建请求
     * @param cloneDir   Git Clone 下来的本地目录
     * @param imageDir   镜像需要存储的目标目录 (仅限 imagePush == false)
     * @param imageName  镜像名称
     * @param dockerfile Dockerfile 文件对象
     * @return 返回构建后的 Docker 镜像的绝对路径地址 (imagePush == false 时不为空，否则空字符串)
     */
    private String dockerBuild(
            StringBuilder logContent, ImageBuilderCreateReq request, Path cloneDir, Path imageDir,
            String imageName, Path dockerfile, String streamKey) {
        List<String> buildArgs = new ArrayList<>();
        if (request.getArgs() != null && request.getArgs().size() > 0) {
            for (Map.Entry<String, String> entry : request.getArgs().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                buildArgs.add("--build-arg");
                buildArgs.add(String.format("%s=%s", key, value));
            }
        }

        String remoteDaemon = imageBuilderProperties.getRemoteDaemon();
        String armRemoteDaemon = imageBuilderProperties.getArmRemoteDaemon();
        String dockerBin = imageBuilderProperties.getDockerBin();
        boolean useSudo = imageBuilderProperties.isUseSudo();
        log.info("prepare to run docker build, armRemoteDaemon={}|imageName={}|request={}",
                armRemoteDaemon, imageName, JSONObject.toJSONString(request));
        String dockerTarget = "";
        if (StringUtils.isNotEmpty(remoteDaemon)) {
            dockerTarget = String.format("-H %s", remoteDaemon);
        } else if (StringUtils.isNotEmpty(armRemoteDaemon) && "arm".equals(request.getArch())) {
            dockerTarget = String.format("-H %s", armRemoteDaemon);
        }
        String sudoCommand = "";
        if (useSudo) {
            sudoCommand = "sudo";
        }

        String localDir;
        if (StringUtils.isEmpty(request.getRepoPath())) {
            localDir = cloneDir.toString();
        } else {
            localDir = Paths.get(cloneDir.toString(), request.getRepoPath()).toString();
        }
        List<String> buildCommand = Lists.newArrayList(sudoCommand, dockerBin, dockerTarget, "build", "-t", imageName, "--pull", "--no-cache");
        buildCommand.addAll(buildArgs);
        buildCommand.add("-f");
        buildCommand.add(dockerfile.toString());
        buildCommand.add(".");
        // for internal use
        if ("Internal".equals(System.getenv("CLOUD_TYPE"))) {
            buildCommand.add("--secret");
            buildCommand.add("id=abm-build-secret,src=/etc/abm-build-secret");
        }
        String[] bashBuildCommand = CommandUtil.getBashCommand(buildCommand.toArray(new String[0]));
        logContent.append(String.format("run command: %s\n", String.join(" ", bashBuildCommand)));
        streamLogService.info(streamKey, String.format("run command: %s\n", String.join(" ", bashBuildCommand)));
        String result = CommandUtil.runLocalCommand(bashBuildCommand, Paths.get(localDir).toFile(), new LogOutputStream() {
            @Override
            protected void processLine(String line) {
                streamLogService.info(streamKey, line, log);
            }
        });
        logContent.append(result);

        // 镜像上传或镜像导出
        if (request.isImagePush()) {
            String[] pushCommand = CommandUtil.getBashCommand(new String[]{
                    sudoCommand, dockerBin, dockerTarget, "push", imageName});
            logContent.append(String.format("run command: %s\n", String.join(" ", pushCommand)));
            streamLogService.info(streamKey, String.format("run command: %s\n", String.join(" ", bashBuildCommand)));
            result = CommandUtil.runLocalCommand(pushCommand, new LogOutputStream() {
                @Override
                protected void processLine(String line) {
                    streamLogService.info(streamKey, line, log);
                }
            });
            logContent.append(result);
            return "";
        } else {
            String imagePath = Paths
                    .get(imageDir.toString(), ImageUtil
                            .getImagePath(request.getAppId(), request.getComponentName(), request.getBasename()))
                    .toString();
            String[] exportCommand = CommandUtil.getBashCommand(
                    new String[]{sudoCommand, dockerBin, dockerTarget, "save", imageName, ">", imagePath});
            logContent.append(String.format("run command: %s\n", String.join(" ", exportCommand)));
            streamLogService.info(streamKey, String.format("run command: %s\n", String.join(" ", exportCommand)));
            result = CommandUtil.runLocalCommand(exportCommand, new LogOutputStream() {
                @Override
                protected void processLine(String line) {
                    streamLogService.info(streamKey, line, log);
                }
            });
            logContent.append(result);
            return imagePath;
        }
    }

    /**
     * 渲染 Dockerfile 文件
     *
     * @param logContent 日志 StringBuilder
     * @param request    镜像构建请求
     * @param cloneDir   目标存储目录
     */
    private Path renderDockerfile(StringBuilder logContent, ImageBuilderCreateReq request, Path cloneDir)
            throws IOException {
        Jinjava jinjava = new Jinjava();
        Path dockerfileTemplate;
        String dockerfileTemplateStr = request.getDockerfileTemplate();
        if (StringUtils.isEmpty(dockerfileTemplateStr)) {
            dockerfileTemplateStr = "Dockerfile";
        }
        if (StringUtils.isEmpty(request.getRepoPath())) {
            dockerfileTemplate = Paths.get(cloneDir.toString(), dockerfileTemplateStr);
        } else {
            dockerfileTemplate = Paths.get(cloneDir.toString(), request.getRepoPath(), dockerfileTemplateStr);
        }
        String template = FileUtils.readFileToString(dockerfileTemplate.toFile(), StandardCharsets.UTF_8);
        String renderedTemplate = jinjava.render(template, request.getDockerfileTemplateArgs());
        String dockerfilePath = request.getDockerfileTemplate() + ".__rendered__";
        Path dockerfile;
        if (StringUtils.isEmpty(request.getRepoPath())) {
            dockerfile = Paths.get(cloneDir.toString(), dockerfilePath);
        } else {
            dockerfile = Paths.get(cloneDir.toString(), request.getRepoPath(), dockerfilePath);
        }
        FileUtils.writeStringToFile(dockerfile.toFile(), renderedTemplate, StandardCharsets.UTF_8);
        logContent.append(String.format("dockerfile has rendered:\n%s", renderedTemplate));
        return dockerfile;
    }

    /**
     * 获取当前实际的目标 imageName 字符串
     *
     * @param request 构建请求
     * @return imageName
     */
    private String getImageName(ImageBuilderCreateReq request) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String imageBasename = String.format("%s-%s-%s", request.getAppId(),
                request.getComponentName(), request.getBasename());

        if (request.isImagePush()) {
            assert !StringUtils.isEmpty(request.getImagePushRegistry());
            if (request.getImagePushUseBranchAsTag() == null || !request.getImagePushUseBranchAsTag()) {
                return String.format("%s/%s:%s", request.getImagePushRegistry(), imageBasename, now);
            } else {
                return String.format("%s/%s:%s", request.getImagePushRegistry(),
                        imageBasename, request.getBranch());
            }
        } else if (StringUtils.isEmpty(request.getImageName())) {
            return String.format("abm.io/abm/%s:%s", imageBasename, now);
        } else {
            return request.getImageName();
        }
    }
}
