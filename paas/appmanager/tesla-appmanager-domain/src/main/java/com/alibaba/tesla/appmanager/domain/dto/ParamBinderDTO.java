package com.alibaba.tesla.appmanager.domain.dto;

import com.alibaba.tesla.appmanager.common.enums.ComponentTypeEnum;

import com.alibaba.tesla.appmanager.common.util.SecurityUtil;
import lombok.Data;

/**
 * @author qianmo.zm@alibaba-inc.com
 * @date 2020/11/03.
 */
@Data
public class ParamBinderDTO {

    /**
     * 参数名,如果是MICROSERVICE，则为Env变量名；如果为RESOURCE_ADDON，则为spec变量名
     */
    private String dataInputName;

    /**
     * 插件标示
     */
    private String componentName;

    /**
     * 组件类型
     */
    private String componentType;

    /**
     * 插件输出参数名
     */
    private String dataOutputName;

    /**
     * 参数缺省值，如果关联了插件，则为空
     */
    private String paramDefaultValue;

    /**
     * 检查参数合法性
     */
    public void checkParameters() {
        SecurityUtil.checkInput(dataInputName);
        SecurityUtil.checkInput(componentName);
        SecurityUtil.checkInput(componentType);
        SecurityUtil.checkInput(dataOutputName);
        SecurityUtil.checkInput(paramDefaultValue);
    }
}
