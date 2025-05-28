package cn.sliew.carp.module.application.oam.model.status;

import lombok.Data;

/**
 * Status defines the loop back status of the abstraction by using CUE template
 */
@Data
public class Status {

    /**
     * CustomStatus defines the custom status message that could display to user
     */
    private String customStatus;

    /**
     * HealthPolicy defines the health check policy for the abstraction
     */
    private String healthPolicy;
}
