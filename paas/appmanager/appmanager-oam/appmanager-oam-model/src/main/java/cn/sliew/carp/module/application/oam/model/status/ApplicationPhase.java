package cn.sliew.carp.module.application.oam.model.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationPhase {

    ApplicationStarting("starting", "ApplicationStarting means the app is preparing for reconcile"),
    ApplicationRendering("rendering", "ApplicationRendering means the app is rendering"),
    ApplicationPolicyGenerating("generatingPolicy", "ApplicationPolicyGenerating means the app is generating policies"),
    ApplicationRunningWorkflow("runningWorkflow", "ApplicationRunningWorkflow means the app is running workflow"),
    ApplicationWorkflowSuspending("workflowSuspending", "ApplicationWorkflowSuspending means the app's workflow is suspending"),
    ApplicationWorkflowTerminated("workflowTerminated", "ApplicationWorkflowTerminated means the app's workflow is terminated"),
    ApplicationWorkflowFailed("workflowFailed", "ApplicationWorkflowFailed means the app's workflow is failed"),
    ApplicationRunning("running", "ApplicationRunning means the app finished rendering and applied result to the cluster"),
    ApplicationUnhealthy("unhealthy", "ApplicationUnhealthy means the app finished rendering and applied result to the cluster, but still unhealthy"),
    ApplicationDeleting("deleting", "ApplicationDeleting means application is being deleted"),
    ;

    private final String code;
    private final String desc;
}
