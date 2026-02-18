package com.resale.resaleinventory.components.internal;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.resale.resaleinventory.components.internal.dto.ModelProjectDTO;
import com.resale.resaleinventory.models.Model;
import com.resale.resaleinventory.models.Project;
import com.resale.resaleinventory.repositories.ModelRepository;
import com.resale.resaleinventory.repositories.ProjectRepository;
import com.resale.resaleinventory.utils.ReturnObject;

@Service
public class InternalService {

    @Autowired
    private ModelRepository modelRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public ReturnObject<ModelProjectDTO> getModelProjectDetails(Integer modelId) {
        if (modelId == null) {
            return new ReturnObject<>("Model ID is required", false, null);
        }

        Model model = modelRepository.findById(modelId).orElse(null);
        if (model == null) {
            return new ReturnObject<>("Model not found", false, null);
        }

        Project project = projectRepository.findById(model.getProjectId()).orElse(null);
        if (project == null) {
            return new ReturnObject<>("Project not found for this model", false, null);
        }

        ModelProjectDTO dto = new ModelProjectDTO(
                model.getId(),
                model.getCode(),
                model.getName(),
                project.getId(),
                project.getNameEn()
        );

        return new ReturnObject<>("Success", true, dto);
    }
}


