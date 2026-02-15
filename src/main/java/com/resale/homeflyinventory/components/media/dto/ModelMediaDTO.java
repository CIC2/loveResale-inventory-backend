package com.resale.homeflyinventory.components.media.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelMediaDTO {
    private Map<String, String> singleImages;
    private Map<String, List<String>> multiImages;
}


