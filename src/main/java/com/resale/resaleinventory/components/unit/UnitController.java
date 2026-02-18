package com.resale.resaleinventory.components.unit;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.resale.resaleinventory.components.unit.dto.UnitComparisonRequestDTO;
import com.resale.resaleinventory.components.unit.dto.UnitDetailsDTO;
import com.resale.resaleinventory.components.unit.dto.UnitListDTO;
import com.resale.resaleinventory.logging.LogActivity;
import com.resale.resaleinventory.models.ActionType;
import com.resale.resaleinventory.models.Unit;
import com.resale.resaleinventory.security.CheckPermission;
import com.resale.resaleinventory.utils.PaginatedResponseDTO;
import com.resale.resaleinventory.utils.ReturnObject;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/unit")
@RequiredArgsConstructor
public class UnitController {

    @Autowired
    UnitService unitService;
    @Autowired
    private final Environment environment;

    @GetMapping("/filter")
    @CheckPermission(value = {"sales:login"})
    @LogActivity(ActionType.UNIT_FILTER)
    public ResponseEntity<ReturnObject<Map<String, Object>>> getUnitFilters(
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) List<Integer> projectIds,
            @RequestParam(required = false) List<Integer> unitTypeIds,
            @RequestParam(required = false) Integer areaFrom,
            @RequestParam(required = false) Integer areaTo,
            @RequestParam(required = false) List<Integer> businessEntityIds,
            @RequestParam(required = false) List<String> region,
            @RequestParam(required = false) List<String> subregion
    ) {
        boolean isSearchAction = (areaFrom != null || areaTo != null ||
                (unitTypeIds != null && !unitTypeIds.isEmpty()) ||
                (projectIds != null && !projectIds.isEmpty()));

        if (isSearchAction && (locationId == null || projectIds == null || projectIds.isEmpty())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ReturnObject<>("locationId and projectIds are mandatory for search", false, null));
        }

        ReturnObject<Map<String, Object>> result =
                unitService.getUnitFilters(locationId, projectIds, unitTypeIds, areaFrom, areaTo, businessEntityIds, region, subregion);

        HttpStatus status = (result.getStatus()) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(result);
    }

    @GetMapping("")
    @CheckPermission(value = {"sales:login"})
    @LogActivity(ActionType.GET_UNITS_SEARCH)
    public ResponseEntity<ReturnObject<PaginatedResponseDTO<UnitListDTO>>> getAllUnits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) List<Integer> projectIds,
            @RequestParam(required = false) List<Integer> unitTypeIds,
            @RequestParam(required = false) Double areaFrom,
            @RequestParam(required = false) Double areaTo,
            @RequestParam(required = false) String modelCode,
            @RequestParam(required = false) List<String> bathrooms,
            @RequestParam(required = false) List<String> bedrooms,
            @RequestParam(required = false) List<String> floors,
            @RequestParam(required = false) String deliveryDateFrom,
            @RequestParam(required = false) String deliveryDateTo,
            @RequestParam(required = false) Double priceFrom,
            @RequestParam(required = false) Double priceTo,
            @RequestParam(required = false) List<Integer> businessEntityIds,
            @RequestParam(required = false) List<String> region,
            @RequestParam(required = false) List<String> subregion
    ) {
        ReturnObject<PaginatedResponseDTO<UnitListDTO>> result = unitService.getAllUnitsWithFilters(
                PageRequest.of(page, size),
                locationId, projectIds, unitTypeIds, areaFrom, areaTo,
                modelCode, bathrooms, bedrooms, floors, deliveryDateFrom, deliveryDateTo, priceFrom, priceTo, businessEntityIds, region, subregion
        );

        return result.getStatus()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }


    @GetMapping("/{unitName}")
    @CheckPermission(value = {"sales:login"})
    @LogActivity(ActionType.GET_UNIT_DETAILS)
    public ResponseEntity<ReturnObject<UnitDetailsDTO>> getUnitDetails(@PathVariable String unitName) {
        ReturnObject<UnitDetailsDTO> result = unitService.getUnitDetails(unitName);
        if (result.getStatus()) {
            return ResponseEntity.ok(result);
        }
        if (unitName == null /*|| unitId <= 0*/) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
        if (result.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }


    @PostMapping("/comparison")
    @LogActivity(ActionType.GET_UNIT_COMPARISON)
    public ResponseEntity<ReturnObject<List<UnitListDTO>>> getUnitComparison(@RequestBody UnitComparisonRequestDTO request) {
        ReturnObject<List<UnitListDTO>> result = unitService.getUnitListForComparison(request.getUnitIds());

        if (!result.getStatus() || result.getData().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }


    @PostMapping("/getUnitsByIds")
    @LogActivity(ActionType.GET_UNITS)
    public ResponseEntity<ReturnObject<List<UnitListDTO>>> getUnitsByIds(
            @RequestBody List<String> unitIds,@RequestHeader(value = "X-Internal-Auth", required = false) String internalToken) {
        // ✅ Security check
        String expectedToken = environment.getProperty("internal.auth.token");
        if (internalToken == null || !internalToken.equals(expectedToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ReturnObject<>( "Unauthorized internal request",false, null));
        }

        try {
            if (unitIds == null || unitIds.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ReturnObject<>("Unit IDs cannot be empty", false, null));
            }

            ReturnObject<List<UnitListDTO>> result = unitService.getUnitsByIds(unitIds);

            if (!result.getStatus() || result.getData() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ReturnObject<>("No units found", false, null));
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ReturnObject<>("Error retrieving units", false, null));
        }
    }


    @PutMapping("/{unitId}/status")
    public ResponseEntity<ReturnObject<Unit>> changeUnitStatus(@PathVariable Integer unitId,
       @RequestHeader(value = "X-Internal-Auth", required = false) String internalToken) {
            // ✅ Security check
            String expectedToken = environment.getProperty("internal.auth.token");
                if (internalToken == null || !internalToken.equals(expectedToken)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new ReturnObject<>( "Unauthorized internal request",false, null));
                }
        ReturnObject<Unit> result = unitService.changeUnitStatus(unitId);

        if (!result.getStatus()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(result);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }


}


