package com.resale.resaleinventory.components.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelComparisonDTO {
    private Integer modelId;
    private String modelCode;
    private String modelName;
    private String modelNameAr;
    private Integer projectId;
    private String projectCode;
    private String projectName;
    private String projectNameAr;
    private String deliveryDateFrom;
    private String deliveryDateTo;
    private String bedroomsFrom;
    private String bedroomsTo;
    private String bathroomsFrom;
    private String bathroomsTo;
    private String floor;
    private Integer areaFrom;
    private Integer areaTo;
    private String finishingEn;
    private String finishingAr;
    private List<String> regions;
    private String mediumImageUrl;



    public ModelComparisonDTO(Integer modelId, String modelCode, String modelName, String modelNameAr,
                              Integer projectId, String projectCode, String projectName, String projectNameAr,
                              String deliveryDateFrom, String deliveryDateTo,
                              String bedroomsFrom, String bedroomsTo,
                              String bathroomsFrom, String bathroomsTo,
                              String floor, Integer areaFrom, Integer areaTo,
                              String finishingEn, String finishingAr) {
        this.modelId = modelId;
        this.modelCode = modelCode;
        this.modelName = modelName;
        this.modelNameAr = modelNameAr;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectName = projectName;
        this.projectNameAr = projectNameAr;
        this.deliveryDateFrom = deliveryDateFrom;
        this.deliveryDateTo = deliveryDateTo;
        this.bedroomsFrom = bedroomsFrom;
        this.bedroomsTo = bedroomsTo;
        this.bathroomsFrom = bathroomsFrom;
        this.bathroomsTo = bathroomsTo;
        this.floor = floor;
        this.areaFrom = areaFrom;
        this.areaTo = areaTo;
        this.finishingEn = finishingEn;
        this.finishingAr = finishingAr;
    }
}


