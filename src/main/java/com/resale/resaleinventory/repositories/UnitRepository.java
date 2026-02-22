package com.resale.resaleinventory.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.resale.resaleinventory.components.model.dto.*;
import com.resale.resaleinventory.components.unit.dto.UnitDetailsDTO;
import com.resale.resaleinventory.components.unit.dto.UnitListDTO;
import com.resale.resaleinventory.models.Unit;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Integer> {


@Query("""
    SELECT new com.resale.resaleinventory.components.unit.dto.UnitListDTO(
        u.id,
        p.id, p.nameEn, p.nameAr,
        p.code,
        ut.name, ut.nameAr,
        m.id, m.code, u.unitModelCode,
        u.nameEn, u.nameAr,
        u.area, u.basePrice, u.address, u.addressAr,
        u.bathroom, u.delivery,
        u.deliveryTextAr, u.deliveryText,
        u.numberOfRooms, u.floorNo,
         u.ruViewDesc,
        m.name,
        m.finishingEn
    )
    FROM Unit u
    LEFT JOIN Project p ON u.projectId = p.id
    LEFT JOIN UsageType ut ON u.usageTypeId = ut.id
    LEFT JOIN Model m ON u.modelId = m.id
    LEFT JOIN Location l ON p.locationId = l.id
    WHERE u.status = 'AV_N'
      AND (:locationId IS NULL OR p.locationId = :locationId)
      AND (:projectIds IS NULL OR p.id IN :projectIds)
      AND (:usageTypeIds IS NULL OR ut.id IN :usageTypeIds)
      AND (:areaFrom IS NULL OR u.area >= :areaFrom)
      AND (:areaTo IS NULL OR u.area <= :areaTo)
      AND (:modelCode IS NULL OR m.code LIKE %:modelCode%)
      AND (:bathrooms IS NULL OR u.bathroom IN :bathrooms)
      AND (:bedrooms IS NULL OR u.numberOfRooms IN :bedrooms)
      AND (:floors IS NULL OR u.floorNo IN :floors)
      AND (
          (:deliveryDateFrom IS NULL AND :deliveryDateTo IS NULL)
          OR (:deliveryDateTo IS NULL AND u.deliveryText = :deliveryDateFrom)
          OR (:deliveryDateTo IS NOT NULL AND u.deliveryText BETWEEN COALESCE(:deliveryDateFrom, u.deliveryText) AND :deliveryDateTo)
      )
      AND (:priceFrom IS NULL OR CAST(u.basePrice AS double) >= :priceFrom)
      AND (:priceTo IS NULL OR CAST(u.basePrice AS double) <= :priceTo)
      AND (:businessEntityIds IS NULL OR u.businessEntityId IN :businessEntityIds)
      AND (:region IS NULL OR u.region IN :region)
      AND (:subregion IS NULL OR u.subregion IN :subregion)

""")
Page<UnitListDTO> findUnits(
        Pageable pageable,
        @Param("locationId") Integer locationId,
        @Param("projectIds") List<Integer> projectIds,
        @Param("usageTypeIds") List<Integer> usageTypeIds,
        @Param("areaFrom") Double areaFrom,
        @Param("areaTo") Double areaTo,
        @Param("modelCode") String modelCode,
        @Param("bathrooms") List<String> bathrooms,
        @Param("bedrooms") List<String> bedrooms,
        @Param("floors") List<String> floors,
        @Param("deliveryDateFrom") String deliveryDateFrom,
        @Param("deliveryDateTo") String deliveryDateTo,
        @Param("priceFrom") Double priceFrom,
        @Param("priceTo") Double priceTo,
        @Param("businessEntityIds") List<Integer> businessEntityIds,
        @Param("region") List<String> region,
        @Param("subregion") List<String> subregion
);


    @Query("""
    SELECT MIN(CAST(u.area AS integer)), MAX(CAST(u.area AS integer))
    FROM Unit u
    WHERE (:locationId IS NULL OR u.projectId IN (
        SELECT p.id FROM Project p WHERE p.locationId = :locationId
    ))
    AND (:projectIds IS NULL OR u.projectId IN :projectIds)
    AND (:unitTypeIds IS NULL OR u.usageTypeId IN :unitTypeIds) AND u.status = "AV_N"
""")
    Object findMinAndMaxAreaByFilters(
            @Param("locationId") Integer locationId,
            @Param("projectIds") List<Integer> projectIds,
            @Param("unitTypeIds") List<Integer> unitTypeIds
    );


    @Query("""
    SELECT DISTINCT new com.resale.resaleinventory.components.model.dto.ModelDTO(m.id, m.code, m.name)
    FROM Unit u
    JOIN Model m ON u.modelId = m.id
    WHERE (:locationId IS NULL OR u.projectId IN (
                SELECT p.id FROM Project p WHERE p.locationId = :locationId
          ))
      AND (:projectIds IS NULL OR u.projectId IN :projectIds)
      AND (:unitTypeIds IS NULL OR u.usageTypeId IN :unitTypeIds)
      AND (:areaFrom IS NULL OR u.area >= :areaFrom)
      AND (:areaTo IS NULL OR u.area <= :areaTo)
      AND m.code IS NOT NULL AND m.code <> ''
      AND u.status = 'AV_N'
""")
    List<ModelDTO> findDistinctUnitModels(
            @Param("locationId") Integer locationId,
            @Param("projectIds") List<Integer> projectIds,
            @Param("unitTypeIds") List<Integer> unitTypeIds,
            @Param("areaFrom") Integer areaFrom,
            @Param("areaTo") Integer areaTo
    );




    @Query("""
    SELECT DISTINCT u.numberOfRooms
    FROM Unit u
    WHERE (:locationId IS NULL OR u.projectId IN (
        SELECT p.id FROM Project p WHERE p.locationId = :locationId
    ))
    AND (:projectIds IS NULL OR u.projectId IN :projectIds)
    AND (:unitTypeIds IS NULL OR u.usageTypeId IN :unitTypeIds)
    AND (:areaFrom IS NULL OR u.area >= :areaFrom)
    AND (:areaTo IS NULL OR u.area <= :areaTo)
    AND u.numberOfRooms IS NOT NULL
    AND u.status = 'AV_N'
    ORDER BY u.numberOfRooms ASC
""")
    List<String> findDistinctBedrooms(
            @Param("locationId") Integer locationId,
            @Param("projectIds") List<Integer> projectIds,
            @Param("unitTypeIds") List<Integer> unitTypeIds,
            @Param("areaFrom") Integer areaFrom,
            @Param("areaTo") Integer areaTo
    );

    @Query("""
    SELECT DISTINCT u.bathroom
    FROM Unit u
    WHERE (:locationId IS NULL OR u.projectId IN (
        SELECT p.id FROM Project p WHERE p.locationId = :locationId
    ))
    AND (:projectIds IS NULL OR u.projectId IN :projectIds)
    AND (:unitTypeIds IS NULL OR u.usageTypeId IN :unitTypeIds)
    AND (:areaFrom IS NULL OR u.area >= :areaFrom)
    AND (:areaTo IS NULL OR u.area <= :areaTo)
    AND u.bathroom IS NOT NULL AND u.bathroom <> ''
    AND u.status = 'AV_N'
    ORDER BY CAST(u.bathroom AS int)
""")
    List<String> findDistinctBathrooms(
            @Param("locationId") Integer locationId,
            @Param("projectIds") List<Integer> projectIds,
            @Param("unitTypeIds") List<Integer> unitTypeIds,
            @Param("areaFrom") Integer areaFrom,
            @Param("areaTo") Integer areaTo
    );

    @Query("""
    SELECT DISTINCT u.floorNo
    FROM Unit u
    WHERE (:locationId IS NULL OR u.projectId IN (
        SELECT p.id FROM Project p WHERE p.locationId = :locationId
    ))
    AND (:projectIds IS NULL OR u.projectId IN :projectIds)
    AND (:unitTypeIds IS NULL OR u.usageTypeId IN :unitTypeIds)
    AND (:areaFrom IS NULL OR u.area >= :areaFrom)
    AND (:areaTo IS NULL OR u.area <= :areaTo)
    AND u.floorNo IS NOT NULL AND u.floorNo <> ''
    AND u.status = 'AV_N'
    ORDER BY CAST(u.floorNo AS integer)
""")
    List<String> findDistinctFloors(
            @Param("locationId") Integer locationId,
            @Param("projectIds") List<Integer> projectIds,
            @Param("unitTypeIds") List<Integer> unitTypeIds,
            @Param("areaFrom") Integer areaFrom,
            @Param("areaTo") Integer areaTo
    );

    @Query("""
        SELECT DISTINCT u.deliveryText
        FROM Unit u
        WHERE (:locationId IS NULL OR u.projectId IN (
        SELECT p.id FROM Project p WHERE p.locationId = :locationId
        
        ))
        AND (:projectIds IS NULL OR u.projectId IN :projectIds)
        AND (:unitTypeIds IS NULL OR u.usageTypeId IN :unitTypeIds)
        AND (:areaFrom IS NULL OR u.area >= :areaFrom)
        AND (:areaTo IS NULL OR u.area <= :areaTo)
        AND u.deliveryText IS NOT NULL AND u.deliveryText <> ''
        AND u.status = 'AV_N'
    """)
    List<String> findDistinctDeliveries(
            @Param("locationId") Integer locationId,
            @Param("projectIds") List<Integer> projectIds,
            @Param("unitTypeIds") List<Integer> unitTypeIds,
            @Param("areaFrom") Integer areaFrom,
            @Param("areaTo") Integer areaTo
    );

    @Query("""
    SELECT
        u.id AS id,
        ut.id AS usageTypeId,
        ut.name AS usageTypeName,
        m.id AS modelId,
        m.code AS modelCode,
        u.unitModelCode AS unitModelCode,
        p.id AS projectId,
        p.nameEn AS projectNameEn,
        p.nameAr AS projectNameAr,
        p.code AS projectCode,
        u.numberOfRooms AS numberOfBedrooms,
        u.bathroom AS numberOfBathrooms,
        u.area AS area,
        u.floorNo AS floor,
        u.deliveryText AS deliveryDate
    FROM Unit u
    LEFT JOIN Project p ON u.projectId = p.id
    LEFT JOIN UsageType ut ON u.usageTypeId = ut.id
    LEFT JOIN Model m ON u.modelId = m.id
    LEFT JOIN Location l ON p.locationId = l.id
                WHERE u.status = 'AV_N'
    AND (:locationId IS NULL OR p.locationId = :locationId)
        AND (:projectIds IS NULL OR p.id IN :projectIds)
        AND (:usageTypeIds IS NULL OR ut.id IN :usageTypeIds)
        AND ((:areaFrom IS NULL AND :areaTo IS NULL) OR u.area BETWEEN COALESCE(:areaFrom, u.area) AND COALESCE(:areaTo, u.area))
        AND (:modelCode IS NULL OR m.code LIKE %:modelCode%)
        AND (:bathrooms IS NULL OR u.bathroom IN :bathrooms)
        AND (:bedrooms IS NULL OR u.numberOfRooms IN :bedrooms)
        AND (:floors IS NULL OR u.floorNo IN :floors)
        AND (
            (:deliveryDateFrom IS NULL AND :deliveryDateTo IS NULL)
            OR (:deliveryDateTo IS NULL AND u.deliveryText = :deliveryDateFrom)
            OR (:deliveryDateTo IS NOT NULL AND u.deliveryText BETWEEN COALESCE(:deliveryDateFrom, u.deliveryText) AND :deliveryDateTo)
        )
""")
    Page<ModelInfoDTO> findFilteredUnits(
            Pageable pageable,
            @Param("locationId") Integer locationId,
            @Param("projectIds") List<Integer> projectIds,
            @Param("usageTypeIds") List<Integer> unitTypeIds,
            @Param("areaFrom") Integer areaFrom,
            @Param("areaTo") Integer areaTo,
            @Param("modelCode") String modelCode,
            @Param("bathrooms") List<String> bathrooms,
            @Param("bedrooms") List<String> bedrooms,
            @Param("floors") List<String> floors,
            @Param("deliveryDateFrom") String deliveryDateFrom,
            @Param("deliveryDateTo") String deliveryDateTo
    );


    @Query("""
    SELECT new com.resale.resaleinventory.components.model.dto.ModelDetailsDTO(
        ut.name,
        l.id,
        l.nameEn,
        l.nameAr,
        p.id,
        p.nameEn,
        p.nameAr,
        p.videoUrl,
        u.deliveryText,
        u.numberOfRooms,
        u.bathroom,
        u.floorNo,
        u.area,
        CASE WHEN u.fGarage IS NOT NULL AND u.fGarage <> '' THEN TRUE ELSE FALSE END
                     
    )
    FROM Unit u
    LEFT JOIN Project p ON u.projectId = p.id
    LEFT JOIN Location l ON p.locationId = l.id
    LEFT JOIN UsageType ut ON u.usageTypeId = ut.id
    WHERE u.id = :unitId
""")
    ModelDetailsDTO findModelDetailsByUnitId(@Param("unitId") Integer unitId);

    @Query("""
                SELECT new com.resale.resaleinventory.components.model.dto.ModelByIdDTO(
                    u.id,
                    m.id,
                    m.code,
                    u.unitModelCode,
                    ut.id,
                    ut.name,
                    l.id,
                    l.nameEn,
                    l.nameAr,
                    p.id,
                    p.nameEn,
                    p.nameAr,
                    p.videoUrl,
                    u.deliveryText,
                    u.numberOfRooms,
                    u.bathroom,
                    u.floorNo,
                    u.area,
                    CASE WHEN u.fGarage IS NOT NULL AND u.fGarage <> '' THEN TRUE ELSE FALSE END,
                    CASE WHEN u.fClub IS NOT NULL AND u.fClub <> '' THEN TRUE ELSE FALSE END,
                    CASE WHEN u.fStorage IS NOT NULL AND u.fStorage <> '' THEN TRUE ELSE FALSE END,
                    CASE WHEN u.fAc IS NOT NULL AND u.fAc <> '' THEN TRUE ELSE FALSE END,
                    p.companyCode,
                    p.code,
                    u.rentalUnit,
                       m.finishingEn,
                       m.finishingAr
                )
                FROM Unit u
                LEFT JOIN Model m ON u.modelId = m.id
                LEFT JOIN Project p ON u.projectId = p.id
                LEFT JOIN Location l ON p.locationId = l.id
                LEFT JOIN UsageType ut ON u.usageTypeId = ut.id
                WHERE u.id = :unitId
            """)
    ModelByIdDTO findModelDetailsByUnit(@Param("unitId") Integer unitId);

    @Query("""
    SELECT DISTINCT u.floorNo
    FROM Unit u
    WHERE u.modelId = :modelId
    AND u.floorNo IS NOT NULL
    AND u.floorNo <> ''
""")
    List<String> findDistinctFloorsByModelId(@Param("modelId") Integer modelId);
    @Query("""
    SELECT MIN(CAST(u.basePrice AS double)), MAX(CAST(u.basePrice AS double))
    FROM Unit u
    WHERE (:locationId IS NULL OR u.projectId IN (
        SELECT p.id FROM Project p WHERE p.locationId = :locationId
    ))
    AND (:projectIds IS NULL OR u.projectId IN :projectIds)
    AND (:unitTypeIds IS NULL OR u.usageTypeId IN :unitTypeIds)
""")
    Object findMinAndMaxBasePrice(
            @Param("locationId") Integer locationId,
            @Param("projectIds") List<Integer> projectIds,
            @Param("unitTypeIds") List<Integer> unitTypeIds
    );

    /*@Query("""
    SELECT new tmg.vso.components.unit.dto.UnitDetailsDTO(
        u.id,
        ut.name,
        u.delivery,
        u.deliveryText,
        u.numberOfRooms,
        u.bathroom,
        u.area,
        l.nameEn,
        u.fGarage,
        p.nameEn
    )
    FROM Unit u
    LEFT JOIN Project p ON u.projectId = p.id
    LEFT JOIN Location l ON p.locationId = l.id
    LEFT JOIN UsageType ut ON u.usageTypeId = ut.id
    WHERE u.id = :unitId
""")
    UnitDetailsDTO findUnitDetailsById(@Param("unitId") Integer unitId);*/



    @Query("""
                SELECT new com.resale.resaleinventory.components.unit.dto.UnitDetailsDTO(
                    u.id,
                    ut.name, ut.nameAr,
                    u.deliveryText,
                    u.deliveryText,
                    u.numberOfRooms,
                    u.bathroom,
                    u.area,
                    l.nameEn, l.nameAr,
                    u.floorNo,
                    CASE WHEN u.fGarage IS NOT NULL AND u.fGarage <> '' THEN TRUE ELSE FALSE END,
                    CASE WHEN u.fClub IS NOT NULL AND u.fClub <> '' THEN TRUE ELSE FALSE END,
                    CASE WHEN u.fStorage IS NOT NULL AND u.fStorage <> '' THEN TRUE ELSE FALSE END,
                    CASE WHEN u.fAc IS NOT NULL AND u.fAc <> '' THEN TRUE ELSE FALSE END,
                    p.nameEn, p.nameAr,
                    p.companyCode,
                    p.code,
                    u.rentalUnit,
                    be.code,
                    u.nameEn,
                    m.id,
                    m.code,
                    u.unitModelCode,
                    p.pdc,
                    p.customerType,
                    p.bankFinance,
                    p.vipCode,
                    p.currencyCheck
                )
                FROM Unit u
                LEFT JOIN Project p ON u.projectId = p.id
                LEFT JOIN Location l ON p.locationId = l.id
                LEFT JOIN UsageType ut ON u.usageTypeId = ut.id
                LEFT JOIN BusinessEntity be ON u.businessEntityId = be.id
                LEFT JOIN Model m ON u.modelId = m.id
                WHERE u.nameEn = :unitName
            """)
    UnitDetailsDTO findUnitDetailsByName(@Param("unitName") String unitName);


    @Query("""
    SELECT new com.resale.resaleinventory.components.unit.dto.UnitListDTO(
        u.id,
        p.id,
        p.nameEn,
        p.nameAr,
        p.code,
        ut.name,
        ut.nameAr,
        m.id,
        m.code,
        u.unitModelCode,
        u.nameEn,
        u.nameAr,
        u.area,
        u.basePrice,
        u.address,
        u.addressAr,
        u.bathroom,
        u.delivery,
        u.deliveryTextAr,
        u.deliveryText,
        u.numberOfRooms,
        u.floorNo,
        u.ruViewDesc,
        m.name,
        m.finishingEn
    )
    FROM Unit u
    LEFT JOIN Project p ON u.projectId = p.id
    LEFT JOIN UsageType ut ON u.usageTypeId = ut.id
    LEFT JOIN Model m ON u.modelId = m.id
    WHERE u.id IN :ids
""")
    List<UnitListDTO> findUnitDetailsForComparison(@Param("ids") List<Integer> ids);

    List<Unit> findByIdIn(List<String> ids);



    @Query("""
    SELECT new com.resale.resaleinventory.components.model.dto.ModelComparisonDTO(
        m.id,
        m.code,
        m.name,
        m.nameAr,
        p.id,
        p.code,
        p.nameEn,
        p.nameAr,
        MIN(u.deliveryText),
        MAX(u.deliveryText),
        MIN(CAST(u.numberOfRooms AS string)),
        MAX(CAST(u.numberOfRooms AS string)),
        MIN(CAST(u.bathroom AS string)),
        MAX(CAST(u.bathroom AS string)),
        '4',
        MIN(CAST(u.area AS integer)),
        MAX(CAST(u.area AS integer)),
        m.finishingEn,
        m.finishingAr
    )
    FROM Unit u
    JOIN Model m ON u.modelId = m.id
    JOIN Project p ON u.projectId = p.id
    WHERE u.modelId IN :modelIds
      AND u.projectId IN :projectIds
    GROUP BY m.id, m.name, m.nameAr, 
             p.id, p.nameEn, p.nameAr,
             m.finishingEn, m.finishingAr
""")
    List<ModelComparisonDTO> findModelDetails(
            @Param("modelIds") List<Integer> modelIds,
            @Param("projectIds") List<Integer> projectIds
    );


    @Query("""
    SELECT DISTINCT u.subregion
    FROM Unit u
    WHERE (:locationId IS NULL OR u.projectId IN
          (SELECT p.id FROM Project p WHERE p.locationId = :locationId))
    AND (:projectIds IS NULL OR u.projectId IN :projectIds)
    AND (:businessEntityIds IS NULL OR u.businessEntityId IN :businessEntityIds)
    AND (:regions IS NULL OR u.region IN :regions)
    AND u.subregion IS NOT NULL AND u.subregion <> ''
""")
    List<String> findDistinctSubregions(
            Integer locationId,
            List<Integer> projectIds,
            List<Integer> businessEntityIds,
            List<String> regions
    );

    @Query("""
    SELECT DISTINCT u.region
    FROM Unit u
    WHERE (:locationId IS NULL OR u.projectId IN
          (SELECT p.id FROM Project p WHERE p.locationId = :locationId))
    AND (:projectIds IS NULL OR u.projectId IN :projectIds)
    AND (:businessEntityIds IS NULL OR u.businessEntityId IN :businessEntityIds)
    AND u.region IS NOT NULL AND u.region <> ''
""")
    List<String> findDistinctRegions(
            Integer locationId,
            List<Integer> projectIds,
            List<Integer> businessEntityIds
    );

    Unit findByNameEn(String unitName);

    @Query("""
    SELECT DISTINCT u.region
    FROM Unit u
    WHERE u.modelId = :modelId
""")
    List<String> findRegionsByModelId(@Param("modelId") Integer modelId);

}

