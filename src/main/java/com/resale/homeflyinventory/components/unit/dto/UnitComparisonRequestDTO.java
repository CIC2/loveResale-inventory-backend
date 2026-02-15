package com.resale.homeflyinventory.components.unit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnitComparisonRequestDTO {
    List<Integer> unitIds;
}


