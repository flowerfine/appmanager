package cn.sliew.carp.module.application.oam.model.status;

import lombok.Data;

@Data
public class PolicyStatus {

    private String name;
    private String type;
    private Object status;
}
