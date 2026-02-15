package com.resale.homeflyinventory.components.media.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MImgsFolderDTO {
    private String folderName;
    private List<MImgsItemDTO> items;
}


