package com.alibaba.tesla.appmanager.domain.req;

import lombok.Data;

/**
 * 应用元信息更新请求
 *
 * @author qianmo.zm@alibaba-inc.com
 */
@Data
public class AppMetaDeleteReq {

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 删除所有应用实例
     */
    private Boolean removeAllInstances = false;

    /**
     * 删除相关的deployConfig
     */
    private Boolean removeAllDeployConfigs = false;

    /**
     * 删除所有的部署记录
     */
    private Boolean removeAllDeployments = false;
}
