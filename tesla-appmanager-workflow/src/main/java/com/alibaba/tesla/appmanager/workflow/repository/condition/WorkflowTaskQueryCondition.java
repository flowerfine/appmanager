package com.alibaba.tesla.appmanager.workflow.repository.condition;

import com.alibaba.tesla.appmanager.common.BaseCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Workflow 实例 Task 查询条件
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTaskQueryCondition extends BaseCondition {

    /**
     * Workflow Task ID
     */
    private Long taskId;

    /**
     * Workflow 实例 ID
     */
    private Long instanceId;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * Task 类型
     */
    private String taskType;

    /**
     * Task 名称
     */
    private String taskName;

    /**
     * Task 状态
     */
    private String taskStatus;

    /**
     * Task 状态 In
     */
    private List<String> taskStatusIn;

    /**
     * 部署单 ID
     */
    private Long deployAppId;

    /**
     * 部署单归属 Unit
     */
    private String deployAppUnitId;

    /**
     * 部署单归属 Namespace
     */
    private String deployAppNamespaceId;

    /**
     * 部署单归属 Stage
     */
    private String deployAppStageId;

    /**
     * 当前任务发起的新 Workflow Instance Id
     */
    private Long deployWorkflowInstanceId;
}
