package cn.sliew.carp.module.application.oam.model.status;

import lombok.Data;

@Data
public class ApplicationTraitStatus {

    private String type;
    private Boolean healthy;
    private String message;
}
