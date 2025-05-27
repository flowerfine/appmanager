package cn.sliew.carp.module.application.oam.model.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConditionType {
    TypeReady("Ready", "Ready"),
    TypeSynced("Ready", "Ready"),
    ;

    private final String value;
    private final String label;
}
