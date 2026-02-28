package com.resale.resaleinventory.components.unit.dto;

import lombok.Data;

@Data
public class CreateUnitRequestDTO {
    private String nameEn;
    private String nameAr;
    private Integer usageTypeId;
    private Integer buildingId;
    private Integer businessEntityId;
    private Integer projectId;
    private Integer modelId;
    private String floorNo;
    private Double coordinatesX;
    private Double coordinatesY;
    private Integer area;
    private String basePrice;
    private String address;
    private String addressAr;
    private String balcony;
    private String bathroom;
    private String delivery;
    private String deliveryDate;
    private String deliveryTextAr;
    private String deliveryText;
    private String downPayment;
    private String fAc;
    private String fClub;
    private String fGarage;
    private String fStorage;
    private String garden;
    private String kitchen;
    private String measUnit;
    private String numberOfRooms;
    private String oldUnitCode;
    private String region;
    private String rentalUnit;
    private String reservationAmount;
    private String ruView;
    private String ruViewDesc;
    private String subregion;
    private String unitDesc;
    private String unitModel;
    private String utility;
    private String numberOfFloors;
    private String unitModelCode;
    private String virtualTourUrl;
}
