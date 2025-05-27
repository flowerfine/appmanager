package cn.sliew.carp.module.application.oam.model.application;

import lombok.Data;

import java.util.List;
import java.util.Properties;

@Data
public class ApplicationComponent {

    private String name;
    private String type;
    private Properties properties;
    private Object inputs;
    private Object outputs;
    private List<ApplicationComponentTrait> traits;
}
