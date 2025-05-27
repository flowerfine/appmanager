package com.alibaba.tesla.appmanager.workflow.repository.domain;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Workflow 实例表
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowInstanceDO {
    /**
     * ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 最后修改时间
     */
    private Date gmtModified;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * Workflow 分类
     */
    private String category;

    /**
     * 开始时间
     */
    private Date gmtStart;

    /**
     * 结束时间
     */
    private Date gmtEnd;

    /**
     * 工作流状态 (PENDING, RUNNING, SUSPEND, SUCCESS, FAILURE, EXCEPTION, TERMINATED)
     */
    private String workflowStatus;

    /**
     * Workflow Configuration SHA256
     */
    private String workflowSha256;

    /**
     * 创建人
     */
    private String workflowCreator;

    /**
     * 乐观锁版本
     */
    private Integer lockVersion;

    /**
     * 当前工作 IP
     */
    private String clientHost;

    /**
     * 工作流执行出错信息 (仅 workflow_status==EXCEPTION 下存在)
     */
    private String workflowErrorMessage;

    /**
     * Workflow Configuration
     */
    private String workflowConfiguration;

    /**
     * Workflow 启动选项 (JSON)
     */
    private String workflowOptions;

    /**
     * Workflow Graph 信息存储 (JSON)
     */
    private String workflowGraph;
}