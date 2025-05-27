package cn.sliew.carp.module.application.oam.model.application;

import lombok.Data;

import java.util.Properties;

@Data
public class ApplicationComponentTrait {

    private String type;
    private Properties properties;
}
