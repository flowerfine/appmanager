package cn.sliew.carp.module.application.oam.model.definition;

import cn.sliew.carp.module.application.oam.model.common.*;
import cn.sliew.carp.module.application.oam.model.status.ConditionStatus;
import cn.sliew.carp.module.application.oam.model.status.Status;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Group(OamConstants.OAM_GROUP)
@Version(OamConstants.OAM_VERSION)
public class WorkloadDefinition extends AbstractSchema {

    private Spec spec;
    private Status status;

    @Data
    public static class Spec {

        private DefinitionRef definitionRef;
        private List<ChildResourceKind> childResourceKinds;
        private Schematic schematic;
        private String revisionLabel;
        private String podSpecPath;
        private Status status;
        private Map extension;
    }

    @Data
    public static class Status extends ConditionStatus {

    }
}
