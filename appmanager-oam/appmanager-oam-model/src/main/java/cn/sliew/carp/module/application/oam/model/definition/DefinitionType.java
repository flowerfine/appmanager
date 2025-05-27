package cn.sliew.carp.module.application.oam.model.definition;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DefinitionType {
    ComponentType("Component", "Component"),
    TraitType("Trait", "Trait"),
    PolicyType("Policy", "Policy"),
    WorkflowStepType("WorkflowStep", "WorkflowStep"),
    ;

    private final String value;
    private final String label;
}
