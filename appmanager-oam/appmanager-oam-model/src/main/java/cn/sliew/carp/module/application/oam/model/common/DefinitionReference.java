package cn.sliew.carp.module.application.oam.model.common;

import lombok.Data;

/**
 * A DefinitionReference refers to a CustomResourceDefinition by name.
 */
@Data
@Deprecated
public class DefinitionReference {

    /**
     * Name of the referenced CustomResourceDefinition.
     */
    private String name;

    /**
     * Version indicate which version should be used if CRD has multiple versions
     * by default it will use the first one if not specified
     */
    private String version;
}
