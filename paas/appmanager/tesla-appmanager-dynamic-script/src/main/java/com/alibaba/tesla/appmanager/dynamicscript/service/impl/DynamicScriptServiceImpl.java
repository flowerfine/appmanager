package com.alibaba.tesla.appmanager.dynamicscript.service.impl;

import com.alibaba.tesla.appmanager.common.BaseRequest;
import com.alibaba.tesla.appmanager.common.exception.AppErrorCode;
import com.alibaba.tesla.appmanager.common.exception.AppException;
import com.alibaba.tesla.appmanager.common.pagination.Pagination;
import com.alibaba.tesla.appmanager.common.util.ClassUtil;
import com.alibaba.tesla.appmanager.common.util.EnvUtil;
import com.alibaba.tesla.appmanager.dynamicscript.repository.DynamicScriptHistoryRepository;
import com.alibaba.tesla.appmanager.dynamicscript.repository.DynamicScriptRepository;
import com.alibaba.tesla.appmanager.dynamicscript.repository.condition.DynamicScriptQueryCondition;
import com.alibaba.tesla.appmanager.dynamicscript.repository.domain.DynamicScriptDO;
import com.alibaba.tesla.appmanager.dynamicscript.repository.domain.DynamicScriptHistoryDO;
import com.alibaba.tesla.appmanager.dynamicscript.service.DynamicScriptService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

/**
 * 动态脚本服务
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
@Slf4j
@Service
public class DynamicScriptServiceImpl implements DynamicScriptService {

    @Autowired
    private DynamicScriptRepository dynamicScriptRepository;
    @Autowired
    private DynamicScriptHistoryRepository dynamicScriptHistoryRepository;

    @Override
    public Pagination<DynamicScriptDO> list(BaseRequest request) {
        DynamicScriptQueryCondition condition = new DynamicScriptQueryCondition();
        ClassUtil.copy(request, condition);
        condition.setWithBlobs(true);
        condition.setEnvId(EnvUtil.currentClusterEnvId());
        List<DynamicScriptDO> page = dynamicScriptRepository.selectByCondition(condition);
        return Pagination.valueOf(page, Function.identity());
    }

    /**
     * 获取动态脚本内容
     *
     * @param condition 查询条件
     * @return DynamicScriptDO
     */
    @Override
    public DynamicScriptDO get(DynamicScriptQueryCondition condition) {
        condition.setEnvId(EnvUtil.currentClusterEnvId());
        return dynamicScriptRepository.getByCondition(condition);
    }

    /**
     * 获取动态脚本列表
     *
     * @param condition 查询条件
     * @return List of DynamicScriptDO
     */
    @Override
    public List<DynamicScriptDO> list(DynamicScriptQueryCondition condition) {
        condition.setEnvId(EnvUtil.currentClusterEnvId());
        return dynamicScriptRepository.selectByCondition(condition);
    }

    /**
     * 删除指定的动态脚本 (所有 node 感知后会自动卸载)
     *
     * @param condition 查询条件
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeScript(DynamicScriptQueryCondition condition) {
        condition.setEnvId(EnvUtil.currentClusterEnvId());
        if (StringUtils.isAnyEmpty(condition.getKind(), condition.getName())) {
            throw new AppException(AppErrorCode.INVALID_USER_ARGS, "must provide kind/name parameters");
        }
        int count = dynamicScriptRepository.deleteByCondition(condition);
        log.info("dynamic script has removed|kind={}|name={}|count={}",
                condition.getKind(), condition.getName(), count);
    }

    /**
     * 初始化脚本
     * <p>
     * * 如果当前记录不存在，则新增
     * * 如果 version 大于当前版本，则覆盖数据库中的已有脚本数据
     * * 如果 version 小于等于当前版本，不进行操作
     *
     * @param condition 查询条件
     * @param revision  提供的脚本版本
     * @param code      Groovy 代码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initScript(DynamicScriptQueryCondition condition, Integer revision, String code) {
        String kind = condition.getKind();
        String name = condition.getName();
        String envId = EnvUtil.currentClusterEnvId();
        DynamicScriptDO script = get(condition);
        if (script != null && script.getCurrentRevision() >= revision) {
            log.info("no need to update dynamic script|kind={}|name={}|revision={}", kind, name, revision);
            return;
        } else if (script == null) {
            dynamicScriptRepository.insert(DynamicScriptDO.builder()
                    .kind(kind)
                    .name(name)
                    .envId(envId)
                    .currentRevision(revision)
                    .code(code)
                    .build());
            log.info("dynamic script has inserted into database|kind={}|name={}|revision={}", kind, name, revision);
        } else {
            script.setCurrentRevision(revision);
            script.setCode(code);
            dynamicScriptRepository.updateByPrimaryKey(script);
            log.info("dynamic script has updated in database|kind={}|name={}|revision={}", kind, name, revision);
        }

        // 插入历史数据
        dynamicScriptHistoryRepository.insert(DynamicScriptHistoryDO.builder()
                .kind(kind)
                .name(name)
                .envId(envId)
                .revision(revision)
                .code(code)
                .build());
    }
}
