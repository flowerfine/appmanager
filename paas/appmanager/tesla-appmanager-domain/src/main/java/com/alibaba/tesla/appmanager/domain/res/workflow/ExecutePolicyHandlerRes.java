package com.alibaba.tesla.appmanager.domain.res.workflow;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.tesla.appmanager.domain.schema.DeployAppSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 执行 Policy Handler 返回结果
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutePolicyHandlerRes implements Serializable {

    /**
     * 修改后的上下文 context 信息
     */
    private JSONObject context;

    /**
     * 修改后的 configuration 部署配置信息
     */
    private DeployAppSchema configuration;
}
