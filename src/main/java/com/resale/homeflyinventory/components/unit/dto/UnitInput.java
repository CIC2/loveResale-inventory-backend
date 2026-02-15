package com.resale.homeflyinventory.components.unit.dto;

import lombok.Data;

@Data
public class UnitInput {
    private String operation="";
    private String partner="";
    private String plan="";
    private String reference="";
    private String unit="";
    private String paymentMethod="";
    private String isPDC="";
    private String vipCode="";
    private String salesEmployee="";
}

