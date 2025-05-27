package cn.sliew.carp.module.application.oam.model.status;

import cn.sliew.carp.module.application.oam.model.common.WorkloadGVK;
import io.fabric8.kubernetes.api.model.ObjectReference;
import lombok.Data;

import java.util.List;

@Data
public class ApplicationComponentStatus {

    private String name;
    private String namespace;
    private String cluster;
    private String env;
    /**
     * WorkloadDefinition is the definition of a WorkloadDefinition, such as deployments/apps.v1
     */
    private WorkloadGVK workloadDefinition;
    private Boolean healthy;
    private String message;
    private List<ApplicationTraitStatus> traits;
    private List<ObjectReference> scopes;
}
