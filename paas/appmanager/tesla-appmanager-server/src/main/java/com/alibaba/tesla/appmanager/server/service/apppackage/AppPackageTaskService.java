package com.alibaba.tesla.appmanager.server.service.apppackage;

import com.alibaba.tesla.appmanager.common.enums.ComponentPackageTaskStateEnum;
import com.alibaba.tesla.appmanager.common.pagination.Pagination;
import com.alibaba.tesla.appmanager.server.repository.condition.AppPackageTaskInQueryCondition;
import com.alibaba.tesla.appmanager.server.repository.condition.AppPackageTaskQueryCondition;
import com.alibaba.tesla.appmanager.server.repository.domain.AppPackageTaskDO;
import com.alibaba.tesla.appmanager.server.repository.domain.ComponentPackageTaskDO;

import java.util.List;

/**
 * 应用包任务服务
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
public interface AppPackageTaskService {

    /**
     * 根据条件过滤应用包任务列表
     *
     * @param condition 过滤条件
     * @return List
     */
    Pagination<AppPackageTaskDO> list(AppPackageTaskQueryCondition condition);

    /**
     * 根据 ID List 列出应用包任务列表 (仅状态，无 Blob 数据)
     *
     * @param condition 过滤条件
     * @return List
     */
    List<AppPackageTaskDO> listIn(AppPackageTaskInQueryCondition condition);

    /**
     * 根据条件获取指定的应用包任务
     *
     * @param condition 过滤条件
     * @return 单个对象
     */
    AppPackageTaskDO get(AppPackageTaskQueryCondition condition);

    /**
     * 根据条件删除包任务
     *
     * @param condition 查询条件
     */
    int delete(AppPackageTaskQueryCondition condition);

    /**
     * 获取指定组件的下一个 Version
     *
     * @param appId         应用 ID
     * @param componentType 组件类型
     * @param componentName 组件名称
     * @param fullVersion   当前提供版本（可为 _，为自动生成）
     * @return next version
     */
    String getComponentNextVersion(
            String appId, String componentType, String componentName, String fullVersion);

    /**
     * 更新指定的组件包任务对象到指定状态
     *
     * @param taskDO 组件包任务对象
     * @param state  目标指定状态
     */
    void updateComponentTaskStatus(ComponentPackageTaskDO taskDO, ComponentPackageTaskStateEnum state);

    /**
     * 刷新指定的应用包任务
     *
     * @param appPackageTaskDO 应用包任务
     */
    void freshAppPackageTask(AppPackageTaskDO appPackageTaskDO);
}
