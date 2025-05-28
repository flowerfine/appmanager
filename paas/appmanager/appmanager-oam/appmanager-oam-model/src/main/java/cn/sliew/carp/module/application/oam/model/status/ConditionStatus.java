package cn.sliew.carp.module.application.oam.model.status;

import lombok.Data;

import java.util.List;

@Data
public class ConditionStatus {

    private List<Condition> conditions;
}
