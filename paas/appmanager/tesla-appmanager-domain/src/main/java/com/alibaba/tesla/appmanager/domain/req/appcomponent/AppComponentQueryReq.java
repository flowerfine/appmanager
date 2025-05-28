package com.alibaba.tesla.appmanager.domain.req.appcomponent;

import com.alibaba.tesla.appmanager.common.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 应用关联组件查询请求
 *
 * @author yaoxing.gyx@alibaba-inc.com
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AppComponentQueryReq extends BaseRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * 分类
     */
    private String category;

    /**
     * Namespace ID
     */
    private String namespaceId;

    /**
     * Stage ID
     */
    private String stageId;

    /**
     * 架构
     */
    private String arch;
}
