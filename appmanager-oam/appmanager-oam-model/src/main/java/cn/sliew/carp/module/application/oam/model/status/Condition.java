package cn.sliew.carp.module.application.oam.model.status;

import lombok.Data;

import java.util.Date;

@Data
public class Condition {

    private ConditionType type;
    private Date lastTransitionTime;
    private String message;
}
