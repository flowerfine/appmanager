package cn.sliew.carp.module.application.oam.model.definition;

import cn.sliew.carp.module.application.oam.model.common.*;
import cn.sliew.carp.module.application.oam.model.status.ConditionStatus;
import cn.sliew.carp.module.application.oam.model.status.Revision;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import lombok.Data;

@Data
@Group(OamConstants.OAM_GROUP)
@Version(OamConstants.OAM_VERSION)
public class WorkflowStepDefinition extends AbstractSchema {

    private Spec spec;
    private Status status;

    @Data
    public static class Spec {

        private DefinitionRef definitionRef;
        private Schematic schematic;
    }

    @Data
    public static class Status extends ConditionStatus {
        private String configMapRef;
        private Revision latestRevision;
    }

}
