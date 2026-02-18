package com.resale.resaleinventory.components.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.resale.resaleinventory.components.media.dto.ModelMediaDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelByIdDTO {
    private Integer unitId;
    private Integer modelId;
    private String modelCode;
    private String unitModelCode;
    Integer usageTypeId;
    String usageType;
    Integer locationId;
    String locationName;
    String locationNameAr;
    Integer projectId;
    String projectName;
    String projectNameAr;
    String projectVideoURL;
    String deliveryDate;
    String bedrooms;
    String bathrooms;
    String floor;
    Integer unitSize;
    Boolean garage;
    Boolean club;
    Boolean storage;
    Boolean ac;
    String companyCode;
    String projectCode;
    String rentalUnit;
    private Boolean isAvailable;
    private String statusDescription;
    private ModelMediaDTO media;
    private String finishingEn;
    private String finishingAr;


    public ModelByIdDTO(
        Integer unitId,
        Integer modelId,
        String modelCode,
        String unitModelCode,
        Integer usageTypeId,
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
        Boolean garage,
        Boolean club,
        Boolean storage,
        Boolean ac,
        String companyCode,
        String projectCode,
        String rentalUnit,
        String finishingEn,
        String finishingAr
) {
    this.unitId = unitId;
    this.modelId = modelId;
    this.modelCode = modelCode;
    this.unitModelCode = unitModelCode;
    this.usageTypeId = usageTypeId;
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
    this.club = club;
    this.storage = storage;
    this.ac = ac;
    this.companyCode = companyCode;
    this.projectCode = projectCode;
    this.rentalUnit = rentalUnit;
    this.finishingEn = finishingEn;
    this.finishingAr = finishingAr;
}
}


