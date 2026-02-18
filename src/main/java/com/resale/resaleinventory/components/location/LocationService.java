package com.resale.resaleinventory.components.location;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.resale.resaleinventory.components.location.dto.LocationDTO;
import com.resale.resaleinventory.components.project.dto.ProjectDTO;
import com.resale.resaleinventory.models.Location;
import com.resale.resaleinventory.models.Project;
import com.resale.resaleinventory.repositories.LocationRepository;
import com.resale.resaleinventory.repositories.ProjectRepository;
import com.resale.resaleinventory.utils.MessageUtil;
import com.resale.resaleinventory.utils.ReturnObject;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationService {
    @Autowired
     LocationRepository locationRepository;
    @Autowired
     ProjectRepository projectRepository;
    @Autowired
    private MessageUtil messageUtil;

    public ReturnObject<LocationDTO> updateLocation(Integer locationId, LocationDTO locationDTO) {
        ReturnObject<LocationDTO> response = new ReturnObject<>();

        try {
            Optional<Location> existingOpt = locationRepository.findById(locationId);
            if (existingOpt.isEmpty()) {
                return new ReturnObject<>(messageUtil.getMessage("location.found.fail"), false, null);
            }

            Location existing = existingOpt.get();

            if (locationRepository.existsByNameEnIgnoreCaseAndIdNot(
                    locationDTO.getNameEn().trim(), locationId)) {

                response.setStatus(false);
                response.setMessage(messageUtil.getMessage("location.nameen.exist"));
                response.setData(null);
                return response;
            }

            if (locationRepository.existsByNameArIgnoreCaseAndIdNot(
                    locationDTO.getNameAr().trim(), locationId)) {

                response.setStatus(false);
                response.setMessage(messageUtil.getMessage("location.namear.exist"));
                response.setData(null);
                return response;
            }

            existing.setNameEn(locationDTO.getNameEn());
            existing.setNameAr(locationDTO.getNameAr());
            locationRepository.save(existing);

            List<Integer> newProjectIds = locationDTO.getProjectIds() != null
                    ? locationDTO.getProjectIds()
                    : Collections.emptyList();

            List<Project> currentlyAssigned = projectRepository.findByLocationId(locationId);
            Set<Integer> currentSet = currentlyAssigned.stream().map(Project::getId).collect(Collectors.toSet());
            Set<Integer> newSet = new HashSet<>(newProjectIds);

            // Determine which projects to assign and which to unassign
            Set<Integer> toAssign = newSet.stream().filter(id -> !currentSet.contains(id)).collect(Collectors.toSet());
            Set<Integer> toUnassign = currentSet.stream().filter(id -> !newSet.contains(id)).collect(Collectors.toSet());

            // Assign new projects
            if (!toAssign.isEmpty()) {
                List<Project> assignProjects = projectRepository.findAllById(toAssign);
                for (Project p : assignProjects) {
                    p.setLocationId(locationId);
                    p.setLocation(existing.getNameEn());
                }
                projectRepository.saveAll(assignProjects);
            }

            if (!toUnassign.isEmpty()) {
                List<Project> unassignProjects = projectRepository.findAllById(toUnassign);
                for (Project p : unassignProjects) {
                    p.setLocationId(null);
                    p.setLocation(null);
                }
                projectRepository.saveAll(unassignProjects);
            }

            List<ProjectDTO> projectDTOs = projectRepository.findByLocationId(locationId)
                    .stream()
                    .map(p -> new ProjectDTO(p.getId(), p.getNameEn(), p.getNameAr(), p.getCode()))
                    .toList();

            long projectCount = projectDTOs.size();

            LocationDTO dto = LocationDTO.builder()
                    .id(existing.getId())
                    .nameEn(existing.getNameEn())
                    .nameAr(existing.getNameAr())
                    .projectCount(projectCount)
                    .projects(projectDTOs)
                    .build();

            response.setStatus(true);
            response.setMessage(messageUtil.getMessage("location.update.success"));
            response.setData(dto);
        }
        catch (Exception ex) {
                response.setStatus(false);
                response.setMessage(messageUtil.getMessage("location.update.failed"));
                response.setData(null);
            }

            return response;
    }




    public ReturnObject<LocationDTO> createLocation(LocationDTO locationDTO) {
        ReturnObject<LocationDTO> returnObject = new ReturnObject<>();

        if (!isValidName(locationDTO.getNameEn())) {
            returnObject.setMessage("Location English name must be between 4 and 30 characters");
            returnObject.setStatus(false);
            return returnObject;
        }

        if (!isValidName(locationDTO.getNameAr())) {
            returnObject.setMessage("Location Arabic name must be between 4 and 30 characters");
            returnObject.setStatus(false);
            return returnObject;
        }

        if (locationRepository.existsByNameEnIgnoreCase(locationDTO.getNameEn().trim())) {
            returnObject.setStatus(false);
            returnObject.setMessage(messageUtil.getMessage("location.nameen.exist"));
            return returnObject;
        }

        if (locationRepository.existsByNameArIgnoreCase(locationDTO.getNameAr().trim())) {
            returnObject.setStatus(false);
            returnObject.setMessage(messageUtil.getMessage("location.namear.exist"));
            return returnObject;
        }

        try {
            Location location = new Location();
            location.setNameEn(locationDTO.getNameEn());
            location.setNameAr(locationDTO.getNameAr());
            Location savedLocation = locationRepository.save(location);

            List<ProjectDTO> projectDTOs = new ArrayList<>();
            if (locationDTO.getProjectIds() != null && !locationDTO.getProjectIds().isEmpty()) {
                List<Project> projects = projectRepository.findAllById(locationDTO.getProjectIds());
                for (Project project : projects) {
                    project.setLocationId(savedLocation.getId());
                    project.setLocation(savedLocation.getNameEn());
                }
                projectRepository.saveAll(projects);

                projectDTOs = projects.stream()
                        .map(p -> new ProjectDTO(p.getId(), p.getNameEn(), p.getNameAr(), p.getCode()))
                        .toList();
            }

            Locale locale = messageUtil.getCurrentLocale();
            boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());

            LocationDTO dto = LocationDTO.builder()
                    .id(savedLocation.getId())
                    .nameEn(savedLocation.getNameEn())
                    .nameAr(savedLocation.getNameAr())
                    .projects(projectDTOs)
                    .build();

            returnObject.setData(dto);
            returnObject.setMessage(messageUtil.getMessage("location.create.success"));
            returnObject.setStatus(true);

        } catch (Exception e) {
            returnObject.setMessage(messageUtil.getMessage("location.create.failed"));
            returnObject.setStatus(false);
            returnObject.setData(null);
        }

        return returnObject;
    }

    private boolean isValidName(String name) {
        if (name == null) return false;
        int length = name.trim().length();
        return length >= 4 && length <= 30;
    }


    public ReturnObject<List<LocationDTO>> getAllLocations() {
        try {
            List<LocationDTO> locationsList = locationRepository.findAllLocations();

            locationsList.forEach(location -> {
                if (location.getProjects() != null) {
                    location.getProjects().forEach(project -> {
                        project.setName(project.getName());
                        project.setNameAr(project.getNameAr());
                    });
                }
            });
            return new ReturnObject<>(
                    messageUtil.getMessage("location.fetch.success"),
                    true,
                    locationsList
            );

        } catch (Exception e) {
            return new ReturnObject<>(
                    messageUtil.getMessage("location.fetch.fail"),
                    false,
                    null
            );
        }
    }


    public ReturnObject<LocationDTO> getLocationDetails(Integer locationId) {
        ReturnObject<LocationDTO> response = new ReturnObject<>();

        Optional<Location> locationOpt = locationRepository.findById(locationId);
        if (locationOpt.isEmpty()) {
            response.setStatus(false);
            response.setMessage(messageUtil.getMessage("location.found.fail"));
            response.setData(null);
            return response;
        }

        Location location = locationOpt.get();

        List<Project> projects = projectRepository.findByLocationId(locationId);
        List<ProjectDTO> projectDTOs = projects.stream()
                .map(p -> new ProjectDTO(p.getId(), p.getNameEn(), p.getNameAr(), p.getCode()))
                .toList();

        Locale locale = messageUtil.getCurrentLocale();
        boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());

        LocationDTO dto = LocationDTO.builder()
                .id(location.getId())
                .nameEn(location.getNameEn())
                .nameAr(location.getNameAr())
                .projects(projectDTOs)
                .projectIds(projectDTOs.stream().map(ProjectDTO::getId).toList())
                .build();

        response.setStatus(true);
        response.setMessage(messageUtil.getMessage("location.details.success"));
        response.setData(dto);
        return response;
    }

    public ReturnObject<String> removeProjectsFromLocation(Integer locationId, List<Integer> projectIds) {
        try {
            List<Project> projects = projectRepository.findAllById(projectIds)
                    .stream()
                    .filter(p -> locationId.equals(p.getLocationId()))
                    .toList();

            for (Project project : projects) {
                project.setLocationId(null);
                project.setLocation(null);
            }

            projectRepository.saveAll(projects);
            return new ReturnObject<>(messageUtil.getMessage("location.project.remove.success"), true, null);

        } catch (Exception e) {
            return new ReturnObject<>(messageUtil.getMessage("location.project.remove.fail"), false, null);
        }
    }


}


