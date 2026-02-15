package com.resale.homeflyinventory.components.unit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsageTypeDTO {
    private Integer id;
    private String name;
    private String nameAr;

}


