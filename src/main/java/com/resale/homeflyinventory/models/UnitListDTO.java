package com.resale.homeflyinventory.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitListDTO {
    private Integer id;
    private String nameEn;
    private String nameAr;
    private Integer usageTypeId;
    private Integer buildingId;
    private Integer projectId;
    private Integer modelId;
    private Integer area;
    private String basePrice;
    private String address;
    private String addressAr;
    private String bathroom;
    private String deliveryDate;
    private String deliveryTextAr;
    private String deliveryText;
    private String numberOfRooms;
    private String unitModel;
}


