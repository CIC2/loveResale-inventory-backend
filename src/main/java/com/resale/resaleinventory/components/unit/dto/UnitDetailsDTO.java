package com.resale.resaleinventory.components.unit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.resale.resaleinventory.components.media.dto.ModelMediaDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnitDetailsDTO {
    private Integer id;
    private String usageTypeName;
    private String usageTypeNameAr;
    private String deliveryDate;
    private String deliveryText;
    private String numberOfRooms;
    private String bathroom;
    private Integer area;
    private String locationName;
    private String locationNameAr;
    private String floorNo;
    private Boolean fGarage;
    private Boolean fClub;
    private Boolean fStorage;
    private Boolean fAc;
    private String projectName;
    private String projectNameAr;
    private String companyCode;
    private String projectCode;
    private String rentalUnit;
    private String businessEntity;
    private String unitName;
    private Integer modelId;
    private String modelCode;
    private String unitModelCode;
    private Boolean isAvailable;
    private String statusDescription;
    private byte isPdc;
    private byte isCustomerType;
    private byte isBankFinance;
    private byte isVipCode;
    private byte isCurrencyCheck;
    private ModelMediaDTO media;

    public UnitDetailsDTO(
            Integer id,
            String usageTypeName,
            String usageTypeNameAr,
            String deliveryDate,
            String deliveryText,
            String numberOfRooms,
            String bathroom,
            Integer area,
            String locationName,
            String locationNameAr,
            String floorNo,
            Boolean fGarage,
            Boolean fClub,
            Boolean fStorage,
            Boolean fAc,
            String projectName,
            String projectNameAr,
            String companyCode,
            String projectCode,
            String rentalUnit,
            String businessEntity,
            String unitName,
            Integer modelId,
            String modelCode,
            String unitModelCode,
            byte pdc,
            byte customerType,
            byte bankFinance,
            byte vipCode,
            byte currencyCheck

    ) {
        this.id = id;
        this.usageTypeName = usageTypeName;
        this.usageTypeNameAr = usageTypeNameAr;
        this.deliveryDate = deliveryDate;
        this.deliveryText = deliveryText;
        this.numberOfRooms = numberOfRooms;
        this.bathroom = bathroom;
        this.area = area;
        this.locationName = locationName;
        this.locationNameAr = locationNameAr;
        this.floorNo = floorNo;
        this.fGarage = fGarage;
        this.fClub = fClub;
        this.fStorage = fStorage;
        this.fAc = fAc;
        this.projectName = projectName;
        this.projectNameAr = projectNameAr;
        this.companyCode = companyCode;
        this.projectCode = projectCode;
        this.rentalUnit = rentalUnit;
        this.businessEntity = businessEntity;
        this.unitName = unitName;
        this.modelId = modelId;
        this.modelCode = modelCode;
        this.unitModelCode = unitModelCode;
        this.isPdc = pdc;
        this.isBankFinance = bankFinance;
        this.isCurrencyCheck = currencyCheck;
        this.isCustomerType = customerType;
        this.isVipCode = vipCode;
    }
}

