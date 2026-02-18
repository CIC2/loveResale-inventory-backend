package com.resale.resaleinventory.components.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModelDTO {
    private Integer id;
    private String code;
    private String name;
}


