package cn.sliew.carp.module.application.oam.model.common;

import cn.sliew.carp.module.application.oam.model.definition.ComponentDefinition;
import cn.sliew.carp.module.application.oam.model.definition.WorkloadDefinition;

public interface WorkloadBinder {

    WorkloadDefinition getWorkload();

    ComponentDefinition getComponent();
}
