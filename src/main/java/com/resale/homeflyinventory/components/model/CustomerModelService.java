package com.resale.homeflyinventory.components.model;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.resale.homeflyinventory.components.location.dto.LocationDTO;
import com.resale.homeflyinventory.components.media.ModelMediaService;
import com.resale.homeflyinventory.components.media.StorageService;
import com.resale.homeflyinventory.components.media.dto.ModelMediaDTO;
import com.resale.homeflyinventory.components.model.dto.*;
import com.resale.homeflyinventory.components.project.dto.ProjectDTO;
import com.resale.homeflyinventory.components.unit.dto.*;
import com.resale.homeflyinventory.constants.UnitStatus;
import com.resale.homeflyinventory.models.InventoryLog;
import com.resale.homeflyinventory.models.Unit;
import com.resale.homeflyinventory.repositories.*;
import com.resale.homeflyinventory.utils.MessageUtil;
import com.resale.homeflyinventory.utils.ReturnObject;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerModelService {

    @Autowired
    UsageTypeRepository usageTypeRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    InventoryLogRepository inventoryLogRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    ModelRepository modelRepository;

    @Autowired
    MessageUtil messageUtil;

    @Autowired
    BusinessEntityRepository businessEntityRepository;
    @Autowired
    ModelMediaService modelMediaService;
    @Autowired
    StorageService storageService;

    public ReturnObject<Map<String, Object>> getUnitFilters(
            Integer locationId,
            List<Integer> projectIds,
            List<Integer> unitTypeIds,
            Integer areaFrom,
            Integer areaTo
    ) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();
            Locale locale = LocaleContextHolder.getLocale();
            boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());
            // locations
            List<LocationDTO> locations = locationRepository.findAllLocations();
            for (LocationDTO location : locations) {
                location.setName(isArabic ? location.getNameAr() : location.getNameEn());
                location.setNameEn(null);
                location.setNameAr(null);
            }
            data.put("locations", locations);

            // projects
            List<ProjectDTO> projects = Collections.emptyList();
            if (locationId != null) {
                projects = projectRepository.findProjectByLocationId(locationId);
                projects.forEach(project -> {
                    project.setName(isArabic ? project.getNameAr() : project.getName());
                    project.setNameAr(null);
                });
            }
            data.put("projects", projects);

            // unit types
            List<UsageTypeDTO> unitTypes = Collections.emptyList();
            if (projectIds != null && !projectIds.isEmpty()) {
                unitTypes = usageTypeRepository.findUnitTypeByProjects(projectIds);
                unitTypes.forEach(unitType -> {
                    unitType.setName(isArabic ? unitType.getNameAr() : unitType.getName());
                    unitType.setNameAr(null);
                });
            }
            data.put("unitTypes", unitTypes);

            // area range
            List<Integer> areaRanges = new ArrayList<>();
            if (locationId != null && projectIds != null && !projectIds.isEmpty()) {
                Object result = unitRepository.findMinAndMaxAreaByFilters(locationId, projectIds, unitTypeIds);
                if (result != null) {
                    Object[] range = (Object[]) result;
                    if (range[0] != null && range[1] != null) {
                        areaRanges.add(((Number) range[0]).intValue());
                        areaRanges.add(((Number) range[1]).intValue());
                    }
                }
            }
            data.put("areaRanges", areaRanges);

            List<ModelDTO> unitModels = Collections.emptyList();
            List<String> bedrooms = Collections.emptyList();
            List<String> bathrooms = Collections.emptyList();
            List<String> floors = Collections.emptyList();
            List<String> deliveries = Collections.emptyList();

            if (locationId != null || (projectIds != null && !projectIds.isEmpty())) {
                unitModels = unitRepository.findDistinctUnitModels(locationId, projectIds, unitTypeIds, areaFrom, areaTo);
                bedrooms = unitRepository.findDistinctBedrooms(locationId, projectIds, unitTypeIds, areaFrom, areaTo);
                bathrooms = unitRepository.findDistinctBathrooms(locationId, projectIds, unitTypeIds, areaFrom, areaTo);
                floors = unitRepository.findDistinctFloors(locationId, projectIds, unitTypeIds, areaFrom, areaTo);
                deliveries = unitRepository.findDistinctDeliveries(locationId, projectIds, unitTypeIds, areaFrom, areaTo);
            }

            data.put("unitModels", unitModels);
            data.put("bedrooms", bedrooms);
            data.put("bathrooms", bathrooms);
            data.put("floors", floors);
            data.put("deliveries", deliveries);

            return new ReturnObject<>(messageUtil.getMessage("unit.fetch.success"), true, data);

        } catch (Exception e) {
            return new ReturnObject<>(messageUtil.getMessage("unit.fetch.fail") + e.getMessage(), false, null);
        }
    }


    public ReturnObject<Map<String, Object>> getAllModelsWithFilters(
            Pageable pageable,
            Integer locationId,
            List<Integer> projectIds,
            List<Integer> unitTypeIds,
            Integer areaFrom,
            Integer areaTo,
            String modelCode,
            List<String> bathrooms,
            List<String> bedrooms,
            List<String> floors,
            String deliveryDateFrom,
            String deliveryDateTo
    ) {
        try {
            Page<ModelInfoDTO> unitsPage = unitRepository.findFilteredUnits(
                    pageable,
                    locationId,
                    projectIds,
                    unitTypeIds,
                    areaFrom,
                    areaTo,
                    modelCode,
                    bathrooms,
                    bedrooms,
                    floors,
                    deliveryDateFrom,
                    deliveryDateTo
            );

            Locale locale = messageUtil.getCurrentLocale();
            boolean isArabic = locale.getLanguage().equals("ar");

            List<Map<String, Object>> translatedModels = unitsPage.getContent().stream()
                    .map(u -> {

                        String imageUrl = getModelMediumImageUrl(
                                u.getProjectCode(),
                                u.getModelCode()
                        );
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("id", u.getId());
                        map.put("usageTypeId", u.getUsageTypeId());
                        map.put("usageTypeName", u.getUsageTypeName());
                        map.put("modelId", u.getModelId());
                        map.put("modelCode", u.getModelCode());
                        map.put("unitModelCode", u.getUnitModelCode());
                        map.put("projectId", u.getProjectId());
                        map.put("projectName", isArabic ? u.getProjectNameAr() : u.getProjectNameEn());
                        map.put("projectCode", u.getProjectCode());
                        map.put("numberOfBedrooms", u.getNumberOfBedrooms());
                        map.put("numberOfBathrooms", u.getNumberOfBathrooms());
                        map.put("area", u.getArea());
                        map.put("floor", u.getFloor());
                        map.put("deliveryDate", u.getDeliveryDate());
                        map.put("modelImageUrl", imageUrl);

                        return map;
                    })
                    .toList();


            Map<String, Object> data = new LinkedHashMap<>();
            data.put("models", translatedModels);
            data.put("currentPage", unitsPage.getNumber());
            data.put("totalElements", unitsPage.getTotalElements());
            data.put("totalPages", unitsPage.getTotalPages());

            return new ReturnObject<>(messageUtil.getMessage("model.fetch.success"), true, data);

        } catch (Exception e) {
            return new ReturnObject<>(
                    messageUtil.getMessage("model.notfound") + e.getMessage(),
                    false,
                    null
            );
        }
    }

    private String getModelMediumImageUrl(String projectCode, String modelCode) {

        String mediumPrefix =
                "assets/assets/Models/" + projectCode + "/" + modelCode + "/MImgs/Medium/";

        List<String> imageKeys = storageService.listObjectsByPrefix(mediumPrefix);

        return imageKeys.stream()
                .findFirst()
                .map(storageService::buildPublicUrl)
                .orElse(null);
    }


    public ReturnObject<ModelByIdDTO> getModelDetails(Integer unitId) {

        ModelByIdDTO dto = unitRepository.findModelDetailsByUnit(unitId);
        if (dto == null) {
            return new ReturnObject<>(messageUtil.getMessage("id.invalid"), false, null);
        }

        Unit unit = unitRepository.findById(unitId).orElse(null);
        if (unit != null) {
            String status = unit.getStatus();
            dto.setIsAvailable(UnitStatus.AVAILABLE.equals(status));
            dto.setStatusDescription(UnitStatus.getStatusDescription(status));
        }

        ModelMediaDTO media = modelMediaService.getMediaForUnit(dto);
        dto.setMedia(media);

        return new ReturnObject<>(
                messageUtil.getMessage("model.details.success"),
                true,
                dto
        );
    }



    public ReturnObject<Page<ModelByIdDTO>> getCustomerModelHistory(
            Integer customerId, int page, int size) {

        // 1. Get DISTINCT model IDs from inventory logs
        List<Integer> modelIds =
                inventoryLogRepository.findDistinctModelIdsByCustomer(8, "CUSTOMER", customerId);

        if (modelIds == null || modelIds.isEmpty()) {
            return new ReturnObject<>(
                    "No model search history found for this customer",
                    true,
                    Page.empty()
            );
        }

        // 2. Pagination calculation
        int start = page * size;
        int end = Math.min(start + size, modelIds.size());

        if (start >= modelIds.size()) {
            return new ReturnObject<>(
                    "No data for this page",
                    true,
                    Page.empty()
            );
        }

        List<Integer> pagedModelIds = modelIds.subList(start, end);

        // 3. Fetch model details
        List<ModelByIdDTO> models = new ArrayList<>();
        for (Integer modelId : pagedModelIds) {
            ReturnObject<ModelByIdDTO> modelResult = getModelDetails(modelId);
            if (modelResult.getStatus() && modelResult.getData() != null) {
                models.add(modelResult.getData());
            }
        }

        // 4. Build Page response
        Pageable pageable = PageRequest.of(page, size);
        Page<ModelByIdDTO> modelPage =
                new PageImpl<>(models, pageable, modelIds.size());

        return new ReturnObject<>(
                "Customer model search history retrieved successfully",
                true,
                modelPage
        );
    }


    public List<ModelComparisonDTO> getModelsDetailsForComparison(List<ModelComparisonRequestDTO> request) {

        if (request == null || request.isEmpty()) {
            return List.of();
        }

        List<Integer> modelIds = request.stream()
                .map(ModelComparisonRequestDTO::getModelId)
                .toList();

        List<Integer> projectIds = request.stream()
                .map(ModelComparisonRequestDTO::getProjectId)
                .toList();

        List<ModelComparisonDTO> dtos = unitRepository.findModelDetails(modelIds, projectIds);

        for (ModelComparisonDTO dto : dtos) {
            List<String> regions = unitRepository.findRegionsByModelId(dto.getModelId());

            // Remove null, empty, or whitespace-only regions
            List<String> cleanedRegions = regions.stream()
                    .filter(r -> r != null && !r.trim().isEmpty())
                    .toList();

            dto.setRegions(cleanedRegions);

            String imageUrl = getModelMediumImageUrl(
                    dto.getProjectCode(),
                    dto.getModelCode()
            );
            dto.setMediumImageUrl(imageUrl);
        }

        return dtos;
    }

}

