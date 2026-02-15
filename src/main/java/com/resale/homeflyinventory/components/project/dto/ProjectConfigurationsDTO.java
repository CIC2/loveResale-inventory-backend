package com.resale.homeflyinventory.components.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectConfigurationsDTO {
    private Boolean pdc;
    private Boolean customerType;
    private Boolean bankFinance;
    private Boolean vipCode;
    private Boolean currencyCheck;
}


