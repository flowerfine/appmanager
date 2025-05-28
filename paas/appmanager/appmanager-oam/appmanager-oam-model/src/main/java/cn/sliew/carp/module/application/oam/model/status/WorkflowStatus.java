package cn.sliew.carp.module.application.oam.model.status;

import io.fabric8.kubernetes.api.model.ObjectReference;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class WorkflowStatus {

    private String appRevision;
    private String mode;
    private String status;
    private String message;
    private Boolean suspend;
    private String suspendState;
    private Boolean terminated;
    private Boolean finished;
    private ObjectReference contextBackend;
    private List<String> steps;
    private Date startTime;
    private Date endTime;
}
