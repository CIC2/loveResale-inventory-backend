package com.resale.resaleinventory.components.unit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsageTypeDTO {
    private Integer id;
    private String name;
    private String nameAr;

}


