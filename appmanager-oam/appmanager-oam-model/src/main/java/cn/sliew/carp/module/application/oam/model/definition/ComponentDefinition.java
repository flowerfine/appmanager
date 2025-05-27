package cn.sliew.carp.module.application.oam.model.definition;

import cn.sliew.carp.module.application.oam.model.common.AbstractSchema;
import cn.sliew.carp.module.application.oam.model.common.OamConstants;
import cn.sliew.carp.module.application.oam.model.common.Schematic;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import lombok.Data;

import java.util.Properties;

@Data
@Group(OamConstants.OAM_GROUP)
@Version(OamConstants.OAM_VERSION)
public class ComponentDefinition extends AbstractSchema {

    private Spec spec;

    @Data
    public static class Spec {

        private Schematic schematic;
        private WorkloadTypeDescriptor workload;
        private Properties properties;
    }

    @Data
    public static class WorkloadTypeDescriptor {

        /**
         * 通过 name 引用 WorkloadDefinition
         */
        private String type;

        /**
         * 通过 group、version 和 kind 引用 WorkloadDefinition。gvk -> group、version、kind
         * 和 type 互斥，只能同时存在一个。
         */
        private WorkloadGVK definition;
    }

    @Data
    public static class WorkloadGVK {

        private String apiVersion;
        private String kind;
    }
}