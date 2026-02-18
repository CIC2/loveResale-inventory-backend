package com.resale.resaleinventory.components.location;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.resale.resaleinventory.components.location.dto.LocationDTO;
import com.resale.resaleinventory.logging.LogActivity;
import com.resale.resaleinventory.models.ActionType;
import com.resale.resaleinventory.security.CheckPermission;
import com.resale.resaleinventory.utils.ReturnObject;

import java.util.List;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    @CheckPermission(value = {"admin:login"})
    @LogActivity(ActionType.CREATE_LOCATION)
    public ResponseEntity<ReturnObject<LocationDTO>> createLocation(@RequestBody LocationDTO request) {
        ReturnObject<LocationDTO> location = locationService.createLocation(request);
        if (Boolean.TRUE.equals(location.getStatus())) {
            return ResponseEntity.ok(location);
        } else {
            return ResponseEntity.badRequest().body(location);
        }
    }

    @PutMapping("/{id}")
    @CheckPermission(value = {"admin:login"})
    @LogActivity(ActionType.UPDATE_LOCATION)
    public ResponseEntity<ReturnObject<LocationDTO>> updateLocation(
            @PathVariable("id") Integer id,
            @RequestBody LocationDTO locationDTO) {
        ReturnObject<LocationDTO> response = locationService.updateLocation(id, locationDTO);
        if (response.getStatus() == null || !response.getStatus()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    @CheckPermission(value = {"admin:login"})
    @LogActivity(ActionType.GET_LOCATIONS)
    public ResponseEntity<ReturnObject<List<LocationDTO>>> getAllLocations() {
        ReturnObject<List<LocationDTO>> result = locationService.getAllLocations();

        return result.getStatus()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @GetMapping("/{id}")
    @CheckPermission(value = {"admin:login"})
    @LogActivity(ActionType.GET_LOCATION_DETAILS)
    public ResponseEntity<ReturnObject<LocationDTO>> getLocationDetails(@PathVariable Integer id) {
        ReturnObject<LocationDTO> response = locationService.getLocationDetails(id);
        if (!response.getStatus() || response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{locationId}/projects")
    @CheckPermission(value = {"admin:login"})
    @LogActivity(ActionType.DELETE_PROJECT_FROM_LOCATION)
    public ResponseEntity<ReturnObject<String>> removeProjectsFromLocation(
            @PathVariable Integer locationId,
            @RequestBody List<Integer> projectIds) {

        ReturnObject<String> response = locationService.removeProjectsFromLocation(locationId, projectIds);
        if (response.getStatus()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}



