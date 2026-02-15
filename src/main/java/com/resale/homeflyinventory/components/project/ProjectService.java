package com.resale.homeflyinventory.components.project;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.resale.homeflyinventory.components.project.dto.ProjectConfigurationsDTO;
import com.resale.homeflyinventory.components.project.dto.ProjectDTO;
import com.resale.homeflyinventory.components.project.dto.ProjectDetailsDTO;
import com.resale.homeflyinventory.models.Project;
import com.resale.homeflyinventory.repositories.ProjectRepository;
import com.resale.homeflyinventory.utils.MessageUtil;
import com.resale.homeflyinventory.utils.ReturnObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private MessageUtil messageUtil;

    public ResponseEntity<?> getAllProjects() {
        try {
            List<ProjectDTO> projects = projectRepository.findAllProjects();

            if (projects == null || projects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ReturnObject<>(
                        messageUtil.getMessage("project.fetch.notfound"),
                        false,
                        null
                ));
            }
            Locale locale = messageUtil.getCurrentLocale();
            boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());

            for (ProjectDTO project : projects) {
                project.setName(isArabic ? project.getNameAr() : project.getName());
                project.setNameAr(null);
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ReturnObject<>(
                    messageUtil.getMessage("project.fetch.success"),
                    true,
                    projects
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ReturnObject<>(
                    messageUtil.getMessage("project.fetch.fail"),
                    false,
                    null
            ));
        }
    }


    public ResponseEntity<?> getProjectById(Integer id) {
        ReturnObject returnObject = new ReturnObject();
        Locale locale = messageUtil.getCurrentLocale();

        Optional<ProjectDetailsDTO> projectDetailsOptional =
                projectRepository.findProjectDetailsById(id, locale.getLanguage());

        if (projectDetailsOptional.isEmpty()) {
            returnObject.setData(null);
            returnObject.setStatus(false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnObject);
        }

        ProjectDetailsDTO projectDetailsDTO = projectDetailsOptional.get();

        returnObject.setData(projectDetailsDTO);
        returnObject.setStatus(true);
        returnObject.setMessage(
                messageUtil.getMessage("project.fetch.project") + " " +
                        projectDetailsDTO.getName() + " " +
                        messageUtil.getMessage("project.fetch.project.found")
        );

        return ResponseEntity.ok(returnObject);
    }


    public ResponseEntity<?> searchProjects(String name, String startDate, String endDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            LocalDateTime startDateTime = null;
            LocalDateTime endDateTime = null;

            if (startDate != null && !startDate.isEmpty()) {
                startDateTime = LocalDate.parse(startDate, formatter).atStartOfDay();
            }

            if (endDate != null && !endDate.isEmpty()) {
                endDateTime = LocalDate.parse(endDate, formatter).atTime(23, 59, 59);
            }

            List<ProjectDTO> projects = projectRepository.searchProjects(name, startDateTime, endDateTime);

            if (projects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ReturnObject<>(messageUtil.getMessage("project.fetch.notfound"), false, null));
            }

            Locale locale = messageUtil.getCurrentLocale();
            boolean isArabic = "ar".equalsIgnoreCase(locale.getLanguage());

            for (ProjectDTO project : projects) {
                project.setName(isArabic ? project.getNameAr() : project.getName());
                project.setNameAr(null);
            }

            return ResponseEntity.ok(
                    new ReturnObject<>(messageUtil.getMessage("project.fetch.success"), true, projects)
            );

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ReturnObject<>(messageUtil.getMessage("project.fetch.fail"), false, null));
        }
    }
    public ResponseEntity<?> updateProjectConfigurations(
            Integer projectId,
            ProjectConfigurationsDTO dto) {

        Locale locale = messageUtil.getCurrentLocale();
        ReturnObject<ProjectDetailsDTO> returnObject = new ReturnObject<>();

        Optional<Project> optionalProject = projectRepository.findById(projectId);

        if (optionalProject.isEmpty()) {
            returnObject.setStatus(false);
            returnObject.setData(null);
            returnObject.setMessage(messageUtil.getMessage("project.not.found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnObject);
        }

        Project project = optionalProject.get();

        // Convert Boolean → byte (1 / 0)
        if (dto.getPdc() != null) {
            project.setPdc(booleanToByte(dto.getPdc()));
        }

        if (dto.getCustomerType() != null) {
            project.setCustomerType(booleanToByte(dto.getCustomerType()));
        }

        if (dto.getBankFinance() != null) {
            project.setBankFinance(booleanToByte(dto.getBankFinance()));
        }

        if (dto.getVipCode() != null) {
            project.setVipCode(booleanToByte(dto.getVipCode()));
        }

        if (dto.getCurrencyCheck() != null) {
            project.setCurrencyCheck(booleanToByte(dto.getCurrencyCheck()));
        }

        project.setUpdatedAt(LocalDateTime.now());
        projectRepository.save(project);

        // 🔁 Re-fetch updated project details (same logic as getProjectById)
        Optional<ProjectDetailsDTO> projectDetailsOptional =
                projectRepository.findProjectDetailsById(projectId, locale.getLanguage());

        if (projectDetailsOptional.isEmpty()) {
            returnObject.setStatus(false);
            returnObject.setData(null);
            returnObject.setMessage(messageUtil.getMessage("project.fetch.project.failed"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(returnObject);
        }

        ProjectDetailsDTO projectDetailsDTO = projectDetailsOptional.get();

        returnObject.setStatus(true);
        returnObject.setData(projectDetailsDTO);
        returnObject.setMessage(
                messageUtil.getMessage("project.update.success") + " " +
                        projectDetailsDTO.getName()
        );

        return ResponseEntity.ok(returnObject);
    }


    private byte booleanToByte(Boolean value) {
        return (byte) (Boolean.TRUE.equals(value) ? 1 : 0);
    }

}





