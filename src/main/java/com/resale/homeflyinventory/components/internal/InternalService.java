package com.resale.homeflyinventory.components.internal;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.resale.homeflyinventory.components.internal.dto.ModelProjectDTO;
import com.resale.homeflyinventory.models.Model;
import com.resale.homeflyinventory.models.Project;
import com.resale.homeflyinventory.repositories.ModelRepository;
import com.resale.homeflyinventory.repositories.ProjectRepository;
import com.resale.homeflyinventory.utils.ReturnObject;

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


