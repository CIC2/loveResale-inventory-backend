package com.resale.homeflyinventory.components.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.resale.homeflyinventory.components.model.dto.ModelDTO;
import com.resale.homeflyinventory.components.model.dto.ModelDetailsDTO;
import com.resale.homeflyinventory.components.model.dto.ModelResponseDTO;
import com.resale.homeflyinventory.models.Model;
import com.resale.homeflyinventory.repositories.ModelRepository;
import com.resale.homeflyinventory.repositories.UnitRepository;
import com.resale.homeflyinventory.utils.MessageUtil;
import com.resale.homeflyinventory.utils.PaginatedResponseDTO;
import com.resale.homeflyinventory.utils.ReturnObject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class SalesModelService {

    @Autowired
    ModelRepository modelRepository;
    @Autowired
    UnitRepository unitRepository;
    @Autowired
    private MessageUtil messageUtil;

    public ReturnObject<ModelDetailsDTO> getModelDetails(Integer modelId) {
        if (modelId == null || modelId <= 0) {
            return new ReturnObject<>(messageUtil.getMessage("model.id.wrong"), false, null);
        }

        List<Integer> unitIds = modelRepository.findUnitIdsByModelId(modelId);
        if (unitIds.isEmpty()) {
            return new ReturnObject<>(messageUtil.getMessage("model.details.notfound"), false, null);
        }

        ModelDetailsDTO details = unitRepository.findModelDetailsByUnitId(unitIds.get(0));
        if (details == null) {
            return new ReturnObject<>(messageUtil.getMessage("model.details.notfound"), false, null);
        }

        List<String> distinctFloors = unitRepository.findDistinctFloorsByModelId(modelId);
        details.setFloors(distinctFloors);

        Locale locale = messageUtil.getCurrentLocale();
        boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());

        details.setLocationName(isArabic ? details.getLocationNameAr() : details.getLocationName());
        details.setProjectName(isArabic ? details.getProjectNameAr() : details.getProjectName());

        details.setLocationNameAr(null);
        details.setProjectNameAr(null);

        return new ReturnObject<>(messageUtil.getMessage("model.details.success"), true, details);
    }


    public ReturnObject<List<ModelDTO>> getAllModels() {
        ReturnObject<List<ModelDTO>> response = new ReturnObject<>();

        List<Model> models = modelRepository.findAll();
        if (models == null || models.isEmpty()) {
            return new ReturnObject<>(messageUtil.getMessage("model.id.wrong"), false, null);
        }

        Locale locale = messageUtil.getCurrentLocale();
        boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());

        List<ModelDTO> localizedModels = models.stream()
                .map(model -> ModelDTO.builder()
                        .id(model.getId())
                        .code(model.getCode())
                        .name(isArabic ? model.getNameAr() : model.getName())
                        .build())
                .toList();

        response.setStatus(true);
        response.setMessage(messageUtil.getMessage("model.fetch.success"));
        response.setData(localizedModels);
        return response;
    }




    public ReturnObject<ModelResponseDTO> updateModel(Integer modelId, Model updatedModel) {
        ReturnObject<ModelResponseDTO> response = new ReturnObject<>();
        if (modelId == null || modelId <= 0) {
            return new ReturnObject<>(messageUtil.getMessage("model.id.wrong"), false, null);
        }

        Optional<Model> existingOpt = modelRepository.findById(modelId);
        if (existingOpt.isEmpty()) {
            response.setStatus(false);
            response.setMessage(messageUtil.getMessage("model.notfound"));
            response.setData(null);
            return response;
        }
        Model existing = existingOpt.get();
        updateExistingModel(existing, updatedModel);
        existing.setUpdatedAt(LocalDateTime.now());
        modelRepository.save(existing);
        Locale locale = messageUtil.getCurrentLocale();
        boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());

        ModelResponseDTO dto = new ModelResponseDTO(
                existing.getId(),
                existing.getCode(),
                isArabic ? existing.getNameAr() : existing.getName(),
                isArabic ? existing.getDescriptionAr() : existing.getDescription(),
                existing.getLayoutUrl()
        );

        return new ReturnObject<>(messageUtil.getMessage("model.update.success"), true, dto);

    }
    private void updateExistingModel(Model existing, Model updatedModel) {
        if (updatedModel == null) return;

        if (updatedModel.getCode() != null)
            existing.setCode(updatedModel.getCode());

        if (updatedModel.getName() != null)
            existing.setName(updatedModel.getName());

        if (updatedModel.getNameAr() != null)
            existing.setNameAr(updatedModel.getNameAr());

        if (updatedModel.getDescription() != null)
            existing.setDescription(updatedModel.getDescription());

        if (updatedModel.getDescriptionAr() != null)
            existing.setDescriptionAr(updatedModel.getDescriptionAr());

        if (updatedModel.getLayoutUrl() != null)
            existing.setLayoutUrl(updatedModel.getLayoutUrl());

    }



}


