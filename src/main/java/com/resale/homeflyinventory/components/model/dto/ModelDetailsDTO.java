package com.resale.homeflyinventory.components.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModelDetailsDTO {
    private Integer unitId;
    private String usageType;
    private Integer locationId;
    private String locationName;
    private String locationNameAr;
    private Integer projectId;
    private String projectName;
    private String projectNameAr;
    private String projectVideoURL;
    private String deliveryDate;
    private String bedrooms;
    private String bathrooms;
    private String floor;
    private List<String> floors;
    private Integer unitSize;
    private Boolean garage;

    ModelDetailsDTO(String usageType, Integer locationId,
                    String locationName, Integer projectId,
                    String projectName, String projectVideoURL,  String deliveryDate,
                    String bedrooms, String bathrooms,
                    String floor, Integer unitSize,
                    Boolean garage) {
        this.usageType = usageType;
        this.locationId = locationId;
        this.locationName = locationName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectVideoURL = projectVideoURL;
        this.deliveryDate = deliveryDate;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.floor = floor;
        this.unitSize = unitSize;
        this.garage = garage;

    }
    public ModelDetailsDTO(
            String usageType,
            Integer locationId,
            String locationName,
            String locationNameAr,
            Integer projectId,
            String projectName,
            String projectNameAr,
            String projectVideoURL,
            String deliveryDate,
            String bedrooms,
            String bathrooms,
            String floor,
            Integer unitSize,
            Boolean garage
    ) {
        this.usageType = usageType;
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationNameAr = locationNameAr;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectNameAr = projectNameAr;
        this.projectVideoURL = projectVideoURL;
        this.deliveryDate = deliveryDate;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.floor = floor;
        this.unitSize = unitSize;
        this.garage = garage;
    }

}


