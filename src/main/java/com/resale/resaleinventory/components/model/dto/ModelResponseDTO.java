package com.resale.resaleinventory.components.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelResponseDTO {
    private Integer id;
    private String code;
    private String name;
    private String description;
    private String layoutUrl;

}


