package com.resale.homeflyinventory.components.project;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.resale.homeflyinventory.components.project.dto.ProjectConfigurationsDTO;
import com.resale.homeflyinventory.logging.LogActivity;
import com.resale.homeflyinventory.models.ActionType;
import com.resale.homeflyinventory.security.CheckPermission;


@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("")
    @LogActivity(ActionType.GET_PROJECTS)
    public ResponseEntity<?> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("{id}")
    @LogActivity(ActionType.GET_PROJECT_DETAILS)
    public ResponseEntity<?> getProjectById(@PathVariable Integer id) {
        return projectService.getProjectById(id);
    }

    @GetMapping("/search")
    @LogActivity(ActionType.GET_PROJECTS)
    public ResponseEntity<?> searchProjects(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        return projectService.searchProjects(name, startDate, endDate);
    }

    @PutMapping("/configurations")
    @LogActivity(ActionType.USER_UPDATE_PROJECT_CONFIGURATIONS)
    public ResponseEntity<?> updateProjectConfigurations(
            @RequestParam Integer projectId,
            @RequestBody ProjectConfigurationsDTO projectConfigurationsDTO) {

        return projectService.updateProjectConfigurations(projectId, projectConfigurationsDTO);
    }


}


