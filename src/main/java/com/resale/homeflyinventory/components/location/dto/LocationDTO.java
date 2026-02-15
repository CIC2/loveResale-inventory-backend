package com.resale.homeflyinventory.components.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.resale.homeflyinventory.components.project.dto.ProjectDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class LocationDTO {
    private Integer id;
    private String name;
    private String nameEn;
    private String nameAr;
    private Long projectCount;
    private List<Integer> projectIds;
    private List<ProjectDTO> projects;


    public LocationDTO(Integer id, String nameEn, String nameAr) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
    }
    public LocationDTO(Integer id, String nameEn, String nameAr, List<ProjectDTO> projects) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        this.projects = projects;
    }
    public LocationDTO(Integer id, String nameEn, String nameAr,
                       List<Integer> projectIds , List<ProjectDTO> projects) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        this.projectIds = projectIds;
        this.projects = projects;
    }
    public LocationDTO(Integer id, List<Integer> projectIds,List<ProjectDTO> projects) {
        this.id = id;
        this.projectIds = projectIds;
        this.projects = projects;
    }
    public LocationDTO(Integer id, List<Integer> projectIds) {
        this.id = id;
        this.projectIds = projectIds;
    }

    public LocationDTO(Integer id, String nameEn, String nameAr, Long projectCount) {
        this.id = id;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        this.projectCount = projectCount;
    }
}

