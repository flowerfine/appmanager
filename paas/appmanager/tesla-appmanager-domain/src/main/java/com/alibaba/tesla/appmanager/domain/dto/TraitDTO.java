package com.alibaba.tesla.appmanager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraitDTO {

    private Long id;

    private Date gmtCreate;

    private Date gmtModified;

    private String name;

    private String className;

    private String definitionRef;

    private String traitDefinition;

    private String label;
}
