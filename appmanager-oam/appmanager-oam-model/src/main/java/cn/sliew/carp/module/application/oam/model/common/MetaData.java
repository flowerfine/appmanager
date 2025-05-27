package cn.sliew.carp.module.application.oam.model.common;

import lombok.Data;

import java.util.Map;

@Data
public class MetaData {

    private String name;
    private Map<String, String> labels;
    /**
     * version 和 description 预定义且推荐使用
     * definition.oam.dev/description: description
     * definition.oam.dev/version: version
     */
    private Map<String, String> annotations;
}
