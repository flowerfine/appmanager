package cn.sliew.carp.module.application.oam.model.common;

import lombok.Data;

/**
 * CUE defines the encapsulation in CUE format
 */
@Data
public class Cue {

    /**
     * Template defines the abstraction template data of the capability, it will replace the old CUE template in extension field.
     * Template is a required field if CUE is defined in Capability Definition.
     */
    private String template;
}
