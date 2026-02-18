package com.resale.resaleinventory.components.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class ProjectDTO {

    private Integer id;
    private String name;
    private String nameAr;
    private String code;
    private String companyCode;

    private Boolean pdc;
    private Boolean customerType;
    private Boolean bankFinance;
    private Boolean vipCode;
    private Boolean currencyCheck;

    private String createdAt;
    private LocalDateTime updatedAt;

    public ProjectDTO(Integer id, String code, String name, String nameAr) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.nameAr = nameAr;
    }

    public ProjectDTO(Integer id, String code, String name, String nameAr,LocalDateTime updatedAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.nameAr = nameAr;
        this.updatedAt = updatedAt;
    }


    public ProjectDTO(
            Integer id,
            String name,
            String nameAr,
            String code,
            String companyCode
//            String updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.nameAr = nameAr;
        this.code = code;
        this.companyCode = companyCode;
//        this.updatedAt = LocalDateTime.parse(updatedAt);
    }
    public ProjectDTO(
            Integer id,
            String name,
            String nameAr,
            String code,
            String companyCode,
            byte pdc,
            byte customerType,
            byte bankFinance,
            byte vipCode,
            byte currencyCheck
    ) {
        this.id = id;
        this.name = name;
        this.nameAr = nameAr;
        this.code = code;
        this.companyCode = companyCode;

        // byte → boolean conversion
        this.pdc = pdc == 1;
        this.customerType = customerType == 1;
        this.bankFinance = bankFinance == 1;
        this.vipCode = vipCode == 1;
        this.currencyCheck = currencyCheck == 1;
    }

}

