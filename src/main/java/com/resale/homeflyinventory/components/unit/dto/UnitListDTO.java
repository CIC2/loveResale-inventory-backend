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
public class UnitListDTO {
    private Integer id;
    private Integer projectId;
    private String projectName;
    private String projectNameAr;
    private String projectCode;
    private String usageTypeName;
    private String usageTypeNameAr;
    private Integer modelId;
    private String modelCode;
    private String unitModelCode;
    private String name;
    private String nameAr;
    private Integer area;
    private String basePrice;
    private String address;
    private String addressAr;
    private String bathroom;
    private String deliveryDate;
    private String deliveryTextAr;
    private String deliveryText;
    private String numberOfRooms;
    private Boolean isAvailable;
    private String statusDescription;
    private String floor;
    private String ruViewDescription;
    private String modelName;
    private String finishing;
    private String modelImageUrl;

    public UnitListDTO(
            Integer id,
            Integer projectId,
            String projectName,
            String projectNameAr,
            String projectCode,
            String usageTypeName,
            String usageTypeNameAr,
            Integer modelId,
            String modelCode,
            String unitModelCode,
            String name,
            String nameAr,
            Integer area,
            String basePrice,
            String address,
            String addressAr,
            String bathroom,
            String deliveryDate,
            String deliveryTextAr,
            String deliveryText,
            String numberOfRooms,
            String floor,
            String ruViewDescription,
            String modelName,
            String finishing
    ) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectNameAr = projectNameAr;
        this.projectCode = projectCode;
        this.usageTypeName = usageTypeName;
        this.usageTypeNameAr = usageTypeNameAr;
        this.modelId = modelId;
        this.modelCode = modelCode;
        this.name = name;
        this.nameAr = nameAr;
        this.area = area;
        this.basePrice = basePrice;
        this.address = address;
        this.addressAr = addressAr;
        this.bathroom = bathroom;
        this.deliveryDate = deliveryDate;
        this.deliveryTextAr = deliveryTextAr;
        this.deliveryText = deliveryText;
        this.numberOfRooms = numberOfRooms;
        this.floor = floor;
        this.ruViewDescription = ruViewDescription;
        this.modelName = modelName;
        this.finishing = finishing;
    }
}

