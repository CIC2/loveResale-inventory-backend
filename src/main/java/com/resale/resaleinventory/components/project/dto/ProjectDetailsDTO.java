package com.resale.resaleinventory.components.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDetailsDTO {
    private Integer id;
    private String code;
    private String name;
    private String description;
    private String companyCode;
    private Double coordinatesX;
    private Double coordinatesY;
    private Integer locationId;
    private String locationName;
    // ✅ New configuration fields
    private Boolean pdc;
    private Boolean customerType;
    private Boolean bankFinance;
    private Boolean vipCode;
    private Boolean currencyCheck;

}


