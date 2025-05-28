package com.alibaba.tesla.appmanager.workflow.repository.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.tesla.appmanager.common.exception.AppErrorCode;
import com.alibaba.tesla.appmanager.common.exception.AppException;
import com.alibaba.tesla.appmanager.common.util.DateUtil;
import com.alibaba.tesla.appmanager.workflow.repository.WorkflowTaskRepository;
import com.alibaba.tesla.appmanager.workflow.repository.condition.WorkflowTaskQueryCondition;
import com.alibaba.tesla.appmanager.workflow.repository.domain.WorkflowTaskDO;
import com.alibaba.tesla.appmanager.workflow.repository.domain.WorkflowTaskDOExample;
import com.alibaba.tesla.appmanager.workflow.repository.mapper.WorkflowTaskDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class WorkflowTaskRepositoryImpl implements WorkflowTaskRepository {

    @Autowired
    private WorkflowTaskDOMapper mapper;

    @Override
    public long countByCondition(WorkflowTaskQueryCondition condition) {
        return mapper.countByExample(buildExample(condition));
    }

    @Override
    public int deleteByCondition(WorkflowTaskQueryCondition condition) {
        return mapper.deleteByExample(buildExample(condition));
    }

    @Override
    public int insert(WorkflowTaskDO record) {
        return mapper.insertSelective(insertDate(record));
    }

    @Override
    public List<WorkflowTaskDO> selectByCondition(WorkflowTaskQueryCondition condition) {
        condition.doPagination();
        if (condition.isWithBlobs()) {
            return mapper.selectByExampleWithBLOBs(buildExample(condition));
        } else {
            return mapper.selectByExample(buildExample(condition));
        }
    }

    @Override
    public WorkflowTaskDO getByCondition(WorkflowTaskQueryCondition condition) {
        List<WorkflowTaskDO> records = selectByCondition(condition);
        if (records.size() == 0) {
            return null;
        } else if (records.size() == 1) {
            return records.get(0);
        } else {
            throw new AppException(AppErrorCode.INVALID_USER_ARGS,
                    String.format("multiple workflow tasks found by condition %s", JSONObject.toJSONString(condition)));
        }
    }

    @Override
    public int updateByCondition(WorkflowTaskDO record, WorkflowTaskQueryCondition condition) {
        return mapper.updateByExampleSelective(updateDate(record), buildExample(condition));
    }

    @Override
    public int updateByPrimaryKey(WorkflowTaskDO record) {
        return mapper.updateByPrimaryKeySelective(updateDate(record));
    }

    /**
     * 获取指定 workflowInstance 中剩余还没有完成的任务清单
     *
     * @param workflowInstanceId Workflow Instance ID
     * @return 待运行 Workflow 任务
     */
    @Override
    public List<WorkflowTaskDO> notFinishedTasks(Long workflowInstanceId) {
        return mapper.notFinishedTasks(workflowInstanceId);
    }

    /**
     * 列出当前所有正在运行中的远程 workflow task
     *
     * @param clientHost 主机
     * @return List or WorkflowTaskDO
     */
    @Override
    public List<WorkflowTaskDO> listRunningRemoteTask(String clientHost) {
        return mapper.listRunningRemoteTask(clientHost);
    }

    private WorkflowTaskDOExample buildExample(WorkflowTaskQueryCondition condition) {
        WorkflowTaskDOExample example = new WorkflowTaskDOExample();
        WorkflowTaskDOExample.Criteria criteria = example.createCriteria();
        if (condition.getTaskId() != null && condition.getTaskId() > 0) {
            criteria.andIdEqualTo(condition.getTaskId());
        }
        if (condition.getInstanceId() != null && condition.getInstanceId() > 0) {
            criteria.andWorkflowInstanceIdEqualTo(condition.getInstanceId());
        }
        if (StringUtils.isNotBlank(condition.getAppId())) {
            criteria.andAppIdEqualTo(condition.getAppId());
        }
        if (StringUtils.isNotBlank(condition.getTaskType())) {
            criteria.andTaskTypeEqualTo(condition.getTaskType());
        }
        if (StringUtils.isNotBlank(condition.getTaskName())) {
            criteria.andTaskNameEqualTo(condition.getTaskName());
        }
        if (StringUtils.isNotBlank(condition.getTaskStatus())) {
            criteria.andTaskStatusEqualTo(condition.getTaskStatus());
        } else if (condition.getTaskStatusIn() != null && condition.getTaskStatusIn().size() > 0) {
            criteria.andTaskStatusIn(condition.getTaskStatusIn());
        }
        if (condition.getDeployAppId() != null && condition.getDeployAppId() > 0) {
            criteria.andDeployAppIdEqualTo(condition.getDeployAppId());
        }
        if (StringUtils.isNotEmpty(condition.getDeployAppUnitId())) {
            criteria.andDeployAppUnitIdEqualTo(condition.getDeployAppUnitId());
        }
        if (StringUtils.isNotEmpty(condition.getDeployAppNamespaceId())) {
            criteria.andDeployAppNamespaceIdEqualTo(condition.getDeployAppNamespaceId());
        }
        if (StringUtils.isNotEmpty(condition.getDeployAppStageId())) {
            criteria.andDeployAppStageIdEqualTo(condition.getDeployAppStageId());
        }
        if (condition.getDeployWorkflowInstanceId() != null && condition.getDeployWorkflowInstanceId() > 0) {
            criteria.andDeployWorkflowInstanceIdEqualTo(condition.getDeployWorkflowInstanceId());
        }
        return example;
    }

    private WorkflowTaskDO insertDate(WorkflowTaskDO record) {
        Date now = DateUtil.now();
        record.setGmtCreate(now);
        record.setGmtModified(now);
        return record;
    }

    private WorkflowTaskDO updateDate(WorkflowTaskDO record) {
        Date now = DateUtil.now();
        record.setGmtModified(now);
        return record;
    }
}
