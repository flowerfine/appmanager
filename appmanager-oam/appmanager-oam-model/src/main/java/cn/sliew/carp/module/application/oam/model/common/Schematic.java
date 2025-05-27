package cn.sliew.carp.module.application.oam.model.common;

import lombok.Data;

@Data
public class Schematic {

    // kubevela 支持 cue、helm、kube
    private String cue;
}
