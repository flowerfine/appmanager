package cn.sliew.carp.module.application.oam.model.status;

import io.fabric8.kubernetes.api.model.ObjectReference;
import lombok.Data;

import java.util.List;

@Data
public class AppStatus extends ConditionStatus {

    /**
     * The generation observed by the application controller.
     */
    private Long observedGeneration;
    private ApplicationPhase status;
    private List<ObjectReference> components;
    private List<ApplicationComponentStatus> services;
    private WorkflowStatus workflow;
    private Revision latestRevision;
//    private List<ClusterObjectReference> appliedResources;
    private List<PolicyStatus> policy;
}
