package cn.sliew.carp.module.application.oam.model.application;

import cn.sliew.carp.module.application.oam.model.common.AbstractSchema;
import cn.sliew.carp.module.application.oam.model.common.OamConstants;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;
import lombok.Data;

import java.util.List;

@Data
@Group(OamConstants.OAM_GROUP)
@Version(OamConstants.OAM_VERSION)
public class Application extends AbstractSchema {

    private Spec spec;

    @Data
    public static class Spec  {

        private List<ApplicationComponent> components;
        private List<ApplicationPolicy> policies;
    }

}
