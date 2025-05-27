package com.alibaba.tesla.appmanager.domain.dto;

import com.alibaba.tesla.appmanager.common.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qianmo.zm@alibaba-inc.com
 * @date 2020/11/26.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitContainerDTO {

    /**
     * 任务名
     */
    private String name;

    /**
     * 任务类型
     */
    private String type;

    /**
     * dockerfile路径
     */
    private String dockerfilePath;

    /**
     * 仓库地址，不填则默认继承container的repoPath
     */
    private String repoPath;

    public String createDockerFileTemplate() {
        return "Dockerfile-" + createContainerName() + ".tpl";
    }

    public String createContainerName() {
        return name;
    }

    /**
     * 检查参数合法性
     */
    public void checkParameters() {
        SecurityUtil.checkInput(name);
        SecurityUtil.checkInput(type);
        SecurityUtil.checkInput(dockerfilePath);
        SecurityUtil.checkInput(repoPath);
    }
}
