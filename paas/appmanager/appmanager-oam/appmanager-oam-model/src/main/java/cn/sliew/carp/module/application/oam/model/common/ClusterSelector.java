package cn.sliew.carp.module.application.oam.model.common;

import lombok.Data;

import java.util.Map;

@Data
public class ClusterSelector {

    private String name;
    private Map<String, String> labels;
}
