package cn.sliew.carp.module.application.oam.model.common;

import lombok.Data;

/**
 * A WorkloadTypeDescriptor refer to a Workload Type
 */
@Data
public class WorkloadTypeDescriptor {

    /**
     * Type ref to a WorkloadDefinition via name
     */
    private String type;

    /**
     * Definition mutually exclusive to workload.type, a embedded WorkloadDefinition
     * 通过 group、version 和 kind 引用 WorkloadDefinition。gvk -> group、version、kind
     * 和 type 互斥，只能同时存在一个。
     */
    private WorkloadGVK definition;
}
