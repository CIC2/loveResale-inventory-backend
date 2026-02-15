package com.resale.homeflyinventory.components.unit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.resale.homeflyinventory.components.location.dto.LocationDTO;
import com.resale.homeflyinventory.components.media.ModelMediaService;
import com.resale.homeflyinventory.components.media.StorageService;
import com.resale.homeflyinventory.components.media.dto.ModelMediaDTO;
import com.resale.homeflyinventory.components.model.dto.ModelDTO;
import com.resale.homeflyinventory.components.project.dto.ProjectDTO;
import com.resale.homeflyinventory.components.unit.dto.*;
import com.resale.homeflyinventory.constants.UnitStatus;
import com.resale.homeflyinventory.models.BusinessEntity;
import com.resale.homeflyinventory.models.Project;
import com.resale.homeflyinventory.models.Unit;
import com.resale.homeflyinventory.repositories.*;
import com.resale.homeflyinventory.utils.MessageUtil;
import com.resale.homeflyinventory.utils.PaginatedResponseDTO;
import com.resale.homeflyinventory.utils.ReturnObject;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UnitService {
    @Autowired
    private UnitRepository unitRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UsageTypeRepository usageTypeRepository;
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
            Integer areaTo,
            List<Integer> businessEntityIds,
            List<String> region,
            List<String> subregion
    ) {
        try {
            Map<String, Object> data = new LinkedHashMap<>();

            Locale locale = messageUtil.getCurrentLocale();
            boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());

            // locations
            List<LocationDTO> locations = locationRepository.findAllLocations();
            locations.forEach(location -> {
                location.setName(isArabic ? location.getNameAr() : location.getNameEn());
                location.setNameAr(null);
                location.setNameEn(null);
            });
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
                unitTypes.forEach(type -> {
                    type.setNameAr(null);
                    type.setName(isArabic ? type.getNameAr() : type.getName());
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

            // price range
            List<Double> priceRanges = new ArrayList<>();
            if (locationId != null && projectIds != null && !projectIds.isEmpty()) {
                Object priceResult = unitRepository.findMinAndMaxBasePrice(locationId, projectIds, unitTypeIds);
                if (priceResult != null) {
                    Object[] priceRange = (Object[]) priceResult;
                    if (priceRange[0] != null && priceRange[1] != null) {
                        priceRanges.add(((Number) priceRange[0]).doubleValue());
                        priceRanges.add(((Number) priceRange[1]).doubleValue());
                    }
                }
            }
            data.put("priceRanges", priceRanges);

            // business entity
            List<BusinessEntityDTO> businessEntities = Collections.emptyList();
            if (projectIds != null && !projectIds.isEmpty()) {
                businessEntities = businessEntityRepository.findByProjectIds(projectIds);
            }
            data.put("businessEntities", businessEntities);

            // region
            List<String> regions = unitRepository.findDistinctRegions(locationId, projectIds, businessEntityIds);
            data.put("regions", regions);

            // subregion
            List<String> subregions = unitRepository.findDistinctSubregions(locationId, projectIds, businessEntityIds, region);
            data.put("subregions", subregions);


            // other filters
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

            return new ReturnObject<>(
                    messageUtil.getMessage("unit.filters.success"),
                    true,
                    data
            );

        } catch (Exception e) {
            return new ReturnObject<>(
                    messageUtil.getMessage("unit.filters.fail"),
                    false,
                    null
            );
        }
    }



    public ReturnObject<PaginatedResponseDTO<UnitListDTO>> getAllUnitsWithFilters(
            Pageable pageable,
            Integer locationId,
            List<Integer> projectIds,
            List<Integer> unitTypeIds,
            Double areaFrom,
            Double areaTo,
            String modelCode,
            List<String> bathrooms,
            List<String> bedrooms,
            List<String> floors,
            String deliveryDateFrom,
            String deliveryDateTo,
            Double priceFrom,
            Double priceTo,
            List<Integer> businessEntityIds,
            List<String> region,
            List<String> subregion
    ) {
        try {
            Page<UnitListDTO> unitPage = unitRepository.findUnits(
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
                    deliveryDateTo,
                    priceFrom,
                    priceTo,
                    businessEntityIds,
                    region,
                    subregion
            );

            Locale locale = messageUtil.getCurrentLocale();
            boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());
            unitPage.getContent().forEach(unit -> {
                unit.setAddress(isArabic ? unit.getAddressAr():unit.getAddress());
                unit.setDeliveryText(isArabic ? unit.getDeliveryTextAr():unit.getDeliveryText());
                unit.setName(isArabic ? unit.getNameAr():unit.getName());
                unit.setUsageTypeName(isArabic ? unit.getUsageTypeNameAr():unit.getUsageTypeName());
                unit.setProjectName(isArabic ? unit.getProjectNameAr():unit.getProjectName());
                unit.setProjectNameAr(null);
                unit.setUsageTypeNameAr(null);
                unit.setNameAr(null);
                unit.setAddressAr(null);
                unit.setDeliveryTextAr(null);
                String imageUrl = getModelMediumImageUrl(unit.getProjectCode(), unit.getModelCode());
                unit.setModelImageUrl(imageUrl);
            });
            PaginatedResponseDTO<UnitListDTO> paginatedResponse = new PaginatedResponseDTO<>(
                    unitPage.getContent(),
                    unitPage.getNumber(),
                    unitPage.getSize(),
                    unitPage.getTotalElements(),
                    unitPage.getTotalPages(),
                    unitPage.isLast()
            );
            return new ReturnObject<>(
                    messageUtil.getMessage("unit.fetch.success"),
                    true,
                    paginatedResponse
            );
        } catch (Exception e) {
            return new ReturnObject<>(
                    messageUtil.getMessage("unit.fetch.fail"),
                    false,
                    null
            );
        }
    }



    public ReturnObject<UnitDetailsDTO> getUnitDetails(String unitName) {
        try {
            if (unitName == null) {
                return new ReturnObject<>(messageUtil.getMessage("unit.invalid.id"), false, null);
            }

            UnitDetailsDTO unitDetails = unitRepository.findUnitDetailsByName(unitName);

            if (unitDetails == null) {
                return new ReturnObject<>(messageUtil.getMessage("unit.fetch.fail"), false, null);
            }

            Locale locale = messageUtil.getCurrentLocale();
            boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());


            unitDetails.setUsageTypeName(isArabic ? unitDetails.getUsageTypeNameAr() : unitDetails.getUsageTypeName());
            unitDetails.setLocationName(isArabic ? unitDetails.getLocationNameAr() : unitDetails.getLocationName());
            unitDetails.setProjectName(isArabic ? unitDetails.getProjectNameAr() : unitDetails.getProjectName());

            Unit unitEntity = unitRepository.findByNameEn(unitName);
            String status = unitEntity.getStatus();
            unitDetails.setIsAvailable(UnitStatus.AVAILABLE.equals(status));
            unitDetails.setStatusDescription(UnitStatus.getStatusDescription(status));

            ModelMediaDTO media = modelMediaService.getMediaForUnitDetails(unitDetails);
            unitDetails.setMedia(media);

            unitDetails.setUsageTypeNameAr(null);
            unitDetails.setLocationNameAr(null);
            unitDetails.setProjectNameAr(null);


            return new ReturnObject<>(messageUtil.getMessage("unit.details.success"), true, unitDetails);

        } catch (Exception e) {
            return new ReturnObject<>(messageUtil.getMessage("unit.details.error"), false, null);
        }
    }


    public ReturnObject<List<UnitListDTO>> getUnitListForComparison(List<Integer> unitIds) {
            if (unitIds == null || unitIds.isEmpty()) {
                return new ReturnObject<>(messageUtil.getMessage("unit.invalid.id"), false, null);
            }

            List <UnitListDTO> detailslist = unitRepository.findUnitDetailsForComparison(unitIds);

            if (detailslist == null || detailslist.isEmpty()) {
                return new ReturnObject<>(messageUtil.getMessage("unit.fetch.fail"), false, null);
            }
            Locale locale = LocaleContextHolder.getLocale();
            boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());
            detailslist.forEach(unit -> {
                unit.setProjectName(isArabic ? unit.getProjectNameAr():unit.getProjectName());
                unit.setProjectNameAr(null);
                unit.setUsageTypeName(isArabic ? unit.getUsageTypeNameAr():unit.getUsageTypeName());
                unit.setUsageTypeNameAr(null);
                unit.setName(isArabic ? unit.getNameAr():unit.getName());
                unit.setNameAr(null);
                unit.setAddress(isArabic ? unit.getAddressAr():unit.getAddress());
                unit.setAddressAr(null);
                unit.setDeliveryText(isArabic ? unit.getDeliveryTextAr():unit.getDeliveryText());
                unit.setDeliveryTextAr(null);
                String imageUrl = getModelMediumImageUrl(unit.getProjectCode(), unit.getModelCode());
                unit.setModelImageUrl(imageUrl);

                Unit entity = unitRepository.findById(unit.getId()).orElse(null);
        if (entity != null) {
            String status = entity.getStatus();
            unit.setIsAvailable(UnitStatus.AVAILABLE.equals(status));
            unit.setStatusDescription(UnitStatus.getStatusDescription(status));
        }
            });
        return new ReturnObject<>(messageUtil.getMessage("unit.details.success"), true,detailslist);
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





    public ReturnObject<List<UnitListDTO>> getUnitsByIds(List<String> unitIds) {
        try {
            List<Unit> units = unitRepository.findByIdIn(unitIds);

            if (units == null || units.isEmpty()) {
                return new ReturnObject<>("No units found", false, null);
            }

            // Convert entity → DTO
            List<UnitListDTO> dtos = units.stream()
                    .map(this::convertToUnitListDto)
                    .collect(Collectors.toList());

            return new ReturnObject<>("Units loaded successfully", true, dtos);

        } catch (Exception e) {
            return new ReturnObject<>("Error fetching units: " + e.getMessage(), false, null);
        }
    }

    private UnitListDTO convertToUnitListDto(Unit unit) {
        UnitListDTO dto = new UnitListDTO();
        dto.setId(unit.getId());
        dto.setName(unit.getOldUnitCode());
        dto.setProjectName(projectRepository.findById(unit.getProjectId()).get().getNameEn());
        dto.setProjectCode(projectRepository.findById(unit.getProjectId()).get().getCode());
        dto.setArea(unit.getArea());
        dto.setUnitModelCode(unit.getUnitModelCode());
        // map other fields...
        return dto;
    }

    public ReturnObject<Unit> changeUnitStatus(Integer unitId) {

        ReturnObject<Unit> response = new ReturnObject<>();

        Optional<Unit> unitOpt = unitRepository.findById(unitId);
        if (unitOpt.isEmpty()) {
            response.setStatus(false);
            response.setMessage("Unit not found");
            return response;
        }

        Unit unit = unitOpt.get();

        if (!UnitStatus.AVAILABLE.equals(unit.getStatus())) {
            response.setStatus(false);
            response.setMessage("Unit status must be AV_N to change to T_LN");
            return response;
        }

        unit.setStatus(UnitStatus.IN_PROCESS);
        unit.setStatusDesc(UnitStatus.getStatusDescription(UnitStatus.IN_PROCESS));
        unit.setUpdatedAt(LocalDateTime.now());

        unitRepository.save(unit);

        response.setStatus(true);
        response.setMessage("Unit status updated successfully");
        response.setData(unit);

        return response;
    }


}



