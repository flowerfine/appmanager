package com.alibaba.tesla.appmanager.domain.req.apppackage;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.tesla.appmanager.common.util.SecurityUtil;
import com.alibaba.tesla.appmanager.domain.dto.ParamBinderDTO;
import com.alibaba.tesla.appmanager.domain.dto.TraitBinderDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author qianmo.zm@alibaba-inc.com
 * @date 2020/11/09.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentBinder {
    /**
     * 组件类型
     */
    private String componentType;

    /**
     * 组件唯一标示
     */
    private String componentName;

    /**
     * 分类
     */
    private String category;

    /**
     * 组件版本
     */
    private String version;

    /**
     * 分支
     */
    private String branch;

    /**
     * 参数绑定
     */
    private List<ParamBinderDTO> paramBinderList;

    /**
     * Trait绑定
     */
    private List<TraitBinderDTO> traitBinderList;

    /**
     * 依赖组件列表
     */
    private List<String> dependComponentList;

    /**
     * 组件配置 Yaml (DeployAppSchema.SpecComponent, 可选，传入则在未来部署的时候，以当前包中携带的配置优先)
     */
    private String componentConfiguration;

    /**
     * 是否为开发模式 (Deprecated)
     */
    private Boolean isDevelop = false;

    /**
     * 是否使用原生 options 配置
     */
    private Boolean useRawOptions = false;

    /**
     * Options
     */
    private JSONObject options = new JSONObject();

    /**
     * 检查参数合法性
     */
    public void checkParameters() {
        SecurityUtil.checkInput(componentType);
        SecurityUtil.checkInput(componentName);
        SecurityUtil.checkInput(category);
        SecurityUtil.checkInput(version);
        SecurityUtil.checkInput(branch);
        if (paramBinderList != null && paramBinderList.size() > 0) {
            paramBinderList.forEach(item -> item.checkParameters());
        }
        if (traitBinderList != null && traitBinderList.size() > 0) {
            traitBinderList.forEach(item -> item.checkParameters());
        }
    }
}
