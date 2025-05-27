package cn.sliew.carp.module.application.oam.model.common;

import lombok.Data;

import java.util.Map;

/**
 * A ChildResourceKind defines a child Kubernetes resource kind with a selector
 */
@Data
public class ChildResourceKind {

    /**
     * APIVersion of the child resource
     */
    private String apiVersion;

    /**
     * Kind of the child resource
     */
    private String kind;

    /**
     * Selector to select the child resources that the workload wants to expose to traits
     */
    private Map<String, String> selector;
}
