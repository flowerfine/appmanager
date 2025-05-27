package cn.sliew.carp.module.application.oam.model.definition;

import cn.sliew.carp.module.application.oam.model.common.AbstractSchema;
import cn.sliew.carp.module.application.oam.model.common.OamConstants;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import lombok.Data;

@Data
@Group(OamConstants.OAM_GROUP)
@Version(OamConstants.OAM_VERSION)
public class WorkflowStepDefinition extends AbstractSchema {

    private Spec spec;

    @Data
    public static class Spec {
        private Semantic schematic;
    }

    @Data
    public static class Semantic {
        // kubevela 支持 cue、helm、kube
    }

}
