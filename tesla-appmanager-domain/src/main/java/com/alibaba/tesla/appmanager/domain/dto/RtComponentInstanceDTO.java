package com.alibaba.tesla.appmanager.domain.dto;

import com.alibaba.fastjson.JSONArray;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 实时组件实例 DTO
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RtComponentInstanceDTO {

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 组件实例 ID
     */
    private String componentInstanceId;

    /**
     * 当前组件归属的应用实例 ID
     */
    private String appInstanceId;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 组件类型
     */
    private String componentType;

    /**
     * 组件类型下的唯一组件标识
     */
    private String componentName;

    /**
     * Cluster ID
     */
    private String clusterId;

    /**
     * Namespace ID
     */
    private String namespaceId;

    /**
     * Stage ID
     */
    private String stageId;

    /**
     * 应用实例版本号 (最近一次应用包部署)
     */
    private String version;

    /**
     * 状态
     */
    private String status;

    /**
     * 监听类型 (KUBERNETES_INFORMER/CRON)
     */
    private String watchKind;

    /**
     * 查询次数 (查询级别升降档匹配使用)
     */
    private Long times;

    /**
     * 当前状态详情 (Yaml Array)
     */
    private JSONArray conditions;

    /**
     * 当前组件的 ComponentSchema YAML 存储
     */
    private String componentSchema;

    /**
     * 关联的应用部署单 ID
     */
    private Long deployAppId;

    /**
     * 关联的组件部署单 ID
     */
    private Long deployComponentId;
}
