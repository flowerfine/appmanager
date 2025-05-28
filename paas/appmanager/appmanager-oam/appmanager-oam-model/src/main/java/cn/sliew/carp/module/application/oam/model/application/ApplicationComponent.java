package cn.sliew.carp.module.application.oam.model.application;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Data
public class ApplicationComponent {

    private String name;
    private String type;
    private String externalRevision;
    private Properties properties;

    private List<String> dependsOn;
    private Object inputs;
    private Object outputs;
    private List<ApplicationComponentTrait> traits;
    private Map<String, String> scopes;
}
