package com.alibaba.tesla.appmanager.server.service.appcomponent;

import com.alibaba.tesla.appmanager.domain.container.AppComponentLocationContainer;
import com.alibaba.tesla.appmanager.server.repository.condition.AppComponentQueryCondition;
import com.alibaba.tesla.appmanager.server.repository.domain.AppComponentDO;

import java.util.List;

/**
 * 应用绑定组件服务
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
public interface AppComponentService {

    /**
     * 获取指定应用下绑定了哪些组件及组件名称
     * @param appId 应用 ID
     * @param isolateNamespaceId 隔离 Namespace ID
     * @param isolateStageId 隔离 Stage ID
     * @return List of AppComponentLocationContainer
     */
    List<AppComponentLocationContainer> getFullComponentRelations(
            String appId, String isolateNamespaceId, String isolateStageId);

    /**
     * 根据条件过滤应用绑定组件
     *
     * @param condition 过滤条件
     * @return List
     */
    List<AppComponentDO> list(AppComponentQueryCondition condition);

    /**
     * 根据条件获取指定的应用绑定组件
     *
     * @param condition 查询条件
     * @return List
     */
    AppComponentDO get(AppComponentQueryCondition condition);

    /**
     * 创建应用绑定组件
     *
     * @param record 绑定记录
     * @return 数据库更新数量
     */
    int create(AppComponentDO record);

    /**
     * 更新应用绑定组件
     *
     * @param record    绑定记录
     * @param condition 查询条件
     * @return 数据库更新数量
     */
    int update(AppComponentDO record, AppComponentQueryCondition condition);

    /**
     * 删除应用绑定组件
     *
     * @param condition 查询条件
     * @return 数据库更新数量
     */
    int delete(AppComponentQueryCondition condition);
}
