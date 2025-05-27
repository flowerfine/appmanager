package cn.sliew.carp.module.application.oam.model.common;

import lombok.Data;

@Data
public class DefinitionRef {

    /**
     * name 和 apiVersion、kind 互斥，只能使用一种方式
     */
    private String name;
    private String apiVersion;
    private String kind;
}
