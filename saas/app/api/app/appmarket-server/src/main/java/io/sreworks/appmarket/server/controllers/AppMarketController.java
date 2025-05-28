package io.sreworks.appmarket.server.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sreworks.domain.DO.App;
import com.alibaba.sreworks.domain.DO.AppMarketEndpoint;
import io.sreworks.appmarket.server.params.AppMarketSyncRemote2LocalParam;
import io.sreworks.appmarket.server.params.AppMarketEndpointUpsertParam;
import io.sreworks.appmarket.server.services.AppMarketService;
import com.alibaba.sreworks.common.util.RegularUtil;
import com.alibaba.sreworks.domain.DO.AppPackage;
import com.alibaba.sreworks.domain.DO.Team;
import com.alibaba.sreworks.domain.repository.AppPackageRepository;
import com.alibaba.sreworks.domain.repository.TeamRepository;
import com.alibaba.sreworks.domain.repository.AppMarketEndpointRepository;
import com.alibaba.sreworks.flyadmin.server.services.FlyadminAppmanagerMarketService;
import com.alibaba.tesla.common.base.TeslaBaseResult;
import com.alibaba.tesla.web.controller.BaseController;

import io.kubernetes.client.openapi.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

/**
 * @author jinghua.yjh
 */
@Slf4j
@RestController
@RequestMapping("/appmarket/appMarket")
@Api(tags = "应用市场")
public class AppMarketController extends BaseController {

    @Autowired
    FlyadminAppmanagerMarketService flyadminAppmanagerMarketService;

    @Autowired
    AppPackageRepository appPackageRepository;

    @Autowired
    AppMarketService appMarketService;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    AppMarketEndpointRepository appMarketEndpointRepository;

    @ApiOperation(value = "listRemoteEndpoints")
    @RequestMapping(value = "listRemoteEndpoints", method = RequestMethod.GET)
    public TeslaBaseResult listRemoteEndpoints() {
        List<AppMarketEndpoint> appMarketEndpoints = appMarketEndpointRepository.findAll();
        List<JSONObject> endpoints = JSONArray.parseArray(JSON.toJSONString(appMarketEndpoints)).toJavaList(JSONObject.class);
        for (JSONObject endpoint : endpoints){
            endpoint.put("config", JSONObject.parseObject(endpoint.getString("config")));
        }
        return buildSucceedResult(endpoints);
    }

    @ApiOperation(value = "upsertRemoteEndpoint")
    @RequestMapping(value = "upsertRemoteEndpoint", method = RequestMethod.POST)
    public TeslaBaseResult upsertRemoteEndpoint(@RequestBody AppMarketEndpointUpsertParam param) {
        AppMarketEndpoint appMarketEndpoint = appMarketEndpointRepository.findFirstByName(param.getName());
        if(appMarketEndpoint == null) {
            appMarketEndpoint = AppMarketEndpoint.builder()
                    .gmtCreate(System.currentTimeMillis() / 1000)
                    .gmtModified(System.currentTimeMillis() / 1000)
                    .creator(getUserEmployeeId())
                    .lastModifier(getUserEmployeeId())
                    .name(param.getName())
                    .config(param.getConfig().toJSONString()).build();
        }else{
            appMarketEndpoint.setConfig(param.getConfig().toJSONString());
        }
        appMarketEndpointRepository.saveAndFlush(appMarketEndpoint);
        return buildSucceedResult(appMarketEndpoint);
    }

    @ApiOperation(value = "deleteRemoteEndpoint")
    @RequestMapping(value = "deleteRemoteEndpoint", method = RequestMethod.DELETE)
    public TeslaBaseResult upsertRemoteEndpoint(Long id) {
        appMarketEndpointRepository.deleteById(id);
        return buildSucceedResult("OK");
    }

    @ApiOperation(value = "list")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public TeslaBaseResult list() {
        List<AppPackage> appPackageList = appPackageRepository.findAllByOnSale(1);
        List<JSONObject> ret = appPackageList.parallelStream().map(appPackage -> {
            JSONObject jsonObject = appPackage.toJsonObject();
            Team team = teamRepository.findFirstById(appPackage.app().getTeamId());
            jsonObject.put("app", appPackage.app());
            jsonObject.put("appName", appPackage.app().getName());
            jsonObject.put("title", appPackage.app().getName());
            jsonObject.put("team", team);
            jsonObject.put("teamName", team.getName());
            jsonObject.put("appComponentList", appPackage.appComponentList());
            jsonObject.put("appComponentSize", appPackage.appComponentList().size());
            return jsonObject;
        }).collect(Collectors.toList());
        RegularUtil.gmt2Date(ret);
        return buildSucceedResult(ret);
    }

    @ApiOperation(value = "download")
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public void download(HttpServletResponse response, HttpServletRequest request, Long appPackageId)
        throws IOException, ApiException {

        AppPackage appPackage = appPackageRepository.findFirstById(appPackageId);
        String zipFilePath = appMarketService.download(appPackage, getUserEmployeeId());

        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        File file = new File(zipFilePath);
        FileInputStream fis = new FileInputStream(file);
        response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
        IOUtils.copy(fis, response.getOutputStream());
        response.flushBuffer();
        fis.close();

    }

    @ApiOperation(value = "listRemote")
    @RequestMapping(value = "listRemote", method = RequestMethod.GET)
    public TeslaBaseResult listRemote() throws IOException {
        return buildSucceedResult(appMarketService.listRemote(getUserEmployeeId()));
    }

    @ApiOperation(value = "syncRemote2Local")
    @RequestMapping(value = "syncRemote2Local", method = RequestMethod.POST)
    public TeslaBaseResult syncRemote2Local(Long appPackageId,
        @RequestBody AppMarketSyncRemote2LocalParam param) throws Exception {
        appMarketService.syncRemote2Local(
            appPackageId,
            param.getTeamId(),
            param.getAppName(),
            getUserEmployeeId(),
            1L,
            1
        );

        return buildSucceedResult("ok");
    }

    @ApiOperation(value = "syncRemote2LocalRepo")
    @RequestMapping(value = "syncRemote2LocalRepo", method = RequestMethod.POST)
    public TeslaBaseResult syncRemote2LocalRepo(Long appPackageId, String appName,
        @RequestBody AppMarketSyncRemote2LocalParam param) throws Exception {
        param.setAppName(appName);
        appMarketService.syncRemote2Local(
            appPackageId,
            param.getTeamId(),
            param.getAppName(),
            getUserEmployeeId(),
            0L,
            1
        );

        return buildSucceedResult("ok");
    }

    @ApiOperation(value = "syncRemote2LocalApp")
    @RequestMapping(value = "syncRemote2LocalApp", method = RequestMethod.POST)
    public TeslaBaseResult syncRemote2LocalApp(Long appPackageId,
        @RequestBody AppMarketSyncRemote2LocalParam param) throws Exception {
        appMarketService.syncRemote2Local(
            appPackageId,
            param.getTeamId(),
            param.getAppName(),
            getUserEmployeeId(),
            1L,
            0
        );

        return buildSucceedResult("ok");
    }
}