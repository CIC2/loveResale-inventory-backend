package com.resale.resaleinventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.resale.resaleinventory.components.project.dto.ProjectDTO;
import com.resale.resaleinventory.components.project.dto.ProjectDetailsDTO;
import com.resale.resaleinventory.models.Project;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("SELECT new com.resale.resaleinventory.components.project.dto.ProjectDTO(p.id,p.code, p.nameEn, p.nameAr ) " +
            "FROM Project p WHERE p.locationId = :locationId")
    List<ProjectDTO> findProjectByLocationId(@Param("locationId") Integer locationId);


    @Query("""
    SELECT new com.resale.resaleinventory.components.project.dto.ProjectDTO(
        p.id,
        p.nameEn,
        p.nameAr,
        p.code,
        p.companyCode,
        p.pdc,
        p.customerType,
        p.bankFinance,
        p.vipCode,
        p.currencyCheck
    )
    FROM Project p
""")
    List<ProjectDTO> findAllProjects();


    @Query("""
    SELECT new com.resale.resaleinventory.components.project.dto.ProjectDetailsDTO(
        p.id,
        p.code,
        CASE WHEN :lang = 'en' THEN p.nameEn ELSE p.nameAr END,
        CASE WHEN :lang = 'en' THEN p.description ELSE p.descriptionAr END,
        p.companyCode,
        p.coordinatesX,
        p.coordinatesY,
        l.id,
        CASE WHEN :lang = 'en' THEN l.nameEn ELSE l.nameAr END,
        CASE WHEN p.pdc = 1 THEN true ELSE false END,
        CASE WHEN p.customerType = 1 THEN true ELSE false END,
        CASE WHEN p.bankFinance = 1 THEN true ELSE false END,
        CASE WHEN p.vipCode = 1 THEN true ELSE false END,
        CASE WHEN p.currencyCheck = 1 THEN true ELSE false END
    )
    FROM Project p
    JOIN Location l ON p.locationId = l.id
    WHERE p.id = :id
""")
    Optional<ProjectDetailsDTO> findProjectDetailsById(
            @Param("id") Integer id,
            @Param("lang") String lang
    );




    List<Project> findByLocationId(Integer locationId);

    @Query("""
    SELECT new com.resale.resaleinventory.components.project.dto.ProjectDTO(
        p.id,
        p.nameEn,
        p.nameAr,
        p.code,
        p.updatedAt
    )
    FROM Project p
    WHERE (:name IS NULL OR LOWER(p.nameEn) LIKE LOWER(CONCAT('%', :name, '%'))
                         OR LOWER(p.nameAr) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:startDate IS NULL OR p.updatedAt >= :startDate)
      AND (:endDate IS NULL OR p.updatedAt <= :endDate)
""")
    List<ProjectDTO> searchProjects(
            @Param("name") String name,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


}


