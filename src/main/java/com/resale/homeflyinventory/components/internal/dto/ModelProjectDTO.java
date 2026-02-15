package com.resale.homeflyinventory.components.internal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelProjectDTO {
    private Integer modelId;
    private String modelCode;
    private String modelName;
    private Integer projectId;
    private String projectName;
}


