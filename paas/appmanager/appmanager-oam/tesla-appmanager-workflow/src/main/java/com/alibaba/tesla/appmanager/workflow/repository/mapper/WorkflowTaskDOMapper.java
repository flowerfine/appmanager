package com.alibaba.tesla.appmanager.workflow.repository.mapper;

import com.alibaba.tesla.appmanager.workflow.repository.domain.WorkflowTaskDO;
import com.alibaba.tesla.appmanager.workflow.repository.domain.WorkflowTaskDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WorkflowTaskDOMapper {
    long countByExample(WorkflowTaskDOExample example);

    int deleteByExample(WorkflowTaskDOExample example);

    int insertSelective(WorkflowTaskDO record);

    List<WorkflowTaskDO> selectByExampleWithBLOBs(WorkflowTaskDOExample example);

    List<WorkflowTaskDO> selectByExample(WorkflowTaskDOExample example);

    int updateByExampleSelective(@Param("record") WorkflowTaskDO record, @Param("example") WorkflowTaskDOExample example);

    int updateByPrimaryKeySelective(WorkflowTaskDO record);

    /**
     * 获取指定 workflowInstance 中剩余还没有完成的任务清单
     *
     * @param workflowInstanceId Workflow Instance ID
     * @return 待运行 Workflow 任务
     */
    List<WorkflowTaskDO> notFinishedTasks(@Param("workflowInstanceId") Long workflowInstanceId);

    /**
     * 列出当前所有正在运行中的远程 workflow task
     *
     * @param clientHost 主机
     * @return List or WorkflowTaskDO
     */
    List<WorkflowTaskDO> listRunningRemoteTask(@Param("clientHost") String clientHost);
}