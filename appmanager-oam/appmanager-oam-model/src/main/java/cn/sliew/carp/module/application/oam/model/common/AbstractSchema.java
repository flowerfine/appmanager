package cn.sliew.carp.module.application.oam.model.common;

import lombok.Data;

@Data
public class AbstractSchema implements Schema {

    private String apiVersion;
    private String kind;
    private MetaData metadata;
}
