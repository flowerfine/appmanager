package cn.sliew.carp.module.application.oam.model.definition;

import cn.sliew.carp.module.application.oam.model.common.AbstractSchema;
import cn.sliew.carp.module.application.oam.model.common.DefinitionRef;
import cn.sliew.carp.module.application.oam.model.common.OamConstants;
import cn.sliew.carp.module.application.oam.model.common.Schematic;
import cn.sliew.carp.module.application.oam.model.status.ConditionStatus;
import cn.sliew.carp.module.application.oam.model.status.Revision;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import lombok.Data;

@Data
@Group(OamConstants.OAM_GROUP)
@Version(OamConstants.OAM_VERSION)
public class PolicyDefinition extends AbstractSchema {

    private Spec spec;
    private Status status;

    @Data
    public static class Spec {
        private DefinitionRef definitionRef;
        private Schematic schematic;
        /**
         * ManageHealthCheck means the policy will handle health checking and skip application controller
         * built-in health checking.
         */
        private Boolean manageHealthCheck;
        private String version;
    }

    @Data
    public static class Status extends ConditionStatus {
        private String configMapRef;
        private Revision latestRevision;
    }
}
