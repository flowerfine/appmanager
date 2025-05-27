package com.alibaba.tesla.appmanager.common.constants;

/**
 * Redis 使用过程中使用到的 Key 常量
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
public final class RedisKeyConstant {

    /**
     * 实时应用实例状态更新 SET
     */
    public static final String RT_APP_INSTANCE_STATUS_UPDATE_SET = "RT_APP_INSTANCE_STATUS_UPDATE_SET";

    /**
     * 应用配置前缀
     */
    public static final String APP_OPTION = "APP_OPTION_";


    public static final String COMPONENT_PACKAGE_TASK_LOG = "COMPONENT_PACKAGE_TASK_LOG";
    public static final String WORKFLOW_TASK_LOG = "WORKFLOW_TASK_LOG";
    public static final String DEPLOY_TASK_LOG = "DEPLOY_TASK_LOG";
    public static final String STREAM_LOG_KEY = "msg";
}
