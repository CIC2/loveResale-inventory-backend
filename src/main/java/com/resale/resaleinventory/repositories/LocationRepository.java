package com.resale.resaleinventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.resale.resaleinventory.components.location.dto.LocationDTO;
import com.resale.resaleinventory.models.Location;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    @Query("""
    SELECT new com.resale.resaleinventory.components.location.dto.LocationDTO(
        l.id,
        l.nameEn,
        l.nameAr,
        COUNT(p.id)
    )
    FROM Location l
    LEFT JOIN Project p ON p.locationId = l.id
    GROUP BY l.id, l.nameEn, l.nameAr
""")
    List<LocationDTO> findAllLocations();

    boolean existsByNameEnIgnoreCase(String nameEn);

    boolean existsByNameArIgnoreCase(String nameAr);

    boolean existsByNameEnIgnoreCaseAndIdNot(String nameEn, Integer id);

    boolean existsByNameArIgnoreCaseAndIdNot(String nameAr, Integer id);
}



