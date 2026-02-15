package com.resale.homeflyinventory.components.model;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.resale.homeflyinventory.components.model.dto.ModelByIdDTO;
import com.resale.homeflyinventory.components.model.dto.ModelComparisonDTO;
import com.resale.homeflyinventory.components.model.dto.ModelComparisonRequestDTO;
import com.resale.homeflyinventory.logging.LogActivity;
import com.resale.homeflyinventory.models.ActionType;
import com.resale.homeflyinventory.security.CheckPermission;
import com.resale.homeflyinventory.utils.ReturnObject;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerModelController {

    @Autowired
    private CustomerModelService customerModelService;

    @GetMapping("filter")
    @LogActivity(ActionType.CUSTOMER_MODEL_FILTER)
    public ResponseEntity<ReturnObject<Map<String, Object>>> getModelFilters(
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) List<Integer> projectIds,
            @RequestParam(required = false) List<Integer> unitTypeIds,
            @RequestParam(required = false) Integer areaFrom,
            @RequestParam(required = false) Integer areaTo
    ) {
        boolean isSearchAction = (areaFrom != null || areaTo != null ||
                (unitTypeIds != null && !unitTypeIds.isEmpty()) ||
                (projectIds != null && !projectIds.isEmpty()));

        if (isSearchAction && (locationId == null || projectIds == null || projectIds.isEmpty())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ReturnObject<>("locationId and projectIds are mandatory for search", false, null));
        }

        ReturnObject<Map<String, Object>> result =
                customerModelService.getUnitFilters(locationId, projectIds, unitTypeIds, areaFrom, areaTo);

        HttpStatus status = (result.getStatus()) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(result);
    }

    @GetMapping("")
    @LogActivity(ActionType.CUSTOMER_GET_MODELS)
    public ReturnObject<Map<String, Object>> getAllModels(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) Integer locationId,
            @RequestParam(required = false) List<Integer> projectIds,
            @RequestParam(required = false) List<Integer> unitTypeIds,
            @RequestParam(required = false) Integer areaFrom,
            @RequestParam(required = false) Integer areaTo,
            @RequestParam(required = false) String modelCode,
            @RequestParam(required = false) List<String> bathrooms,
            @RequestParam(required = false) List<String> bedrooms,
            @RequestParam(required = false) List<String> floors,
            @RequestParam(required = false) String deliveryDateFrom,
            @RequestParam(required = false) String deliveryDateTo
    ) {
        return customerModelService.getAllModelsWithFilters(PageRequest.of(page, size),
                locationId, projectIds, unitTypeIds, areaFrom, areaTo, modelCode,
                bathrooms, bedrooms, floors, deliveryDateFrom, deliveryDateTo
        );
    }


    @GetMapping("/{id}")
    @LogActivity(ActionType.CUSTOMER_GET_MODEL_DETAILS)
    public ResponseEntity<ReturnObject<ModelByIdDTO>> getModelDetails(@PathVariable Integer id) {
        ReturnObject<ModelByIdDTO> result = customerModelService.getModelDetails(id);
        if (!result.getStatus()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping("/comparison")
    @LogActivity(ActionType.CUSTOMER_MODEL_COMPARISON)
    public ResponseEntity<List<ModelComparisonDTO>> getModelDetailsForComparison(@RequestBody List<ModelComparisonRequestDTO> request) {
        List<ModelComparisonDTO> dtos = customerModelService.getModelsDetailsForComparison(request);

        if (dtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/history")
    @CheckPermission(value = {"sales:login"})
    @LogActivity(ActionType.USER_GET_CUSTOMER_MODELS_HISTORY)
    public ResponseEntity<ReturnObject<Page<ModelByIdDTO>>> getCustomerModelHistory(
            @RequestParam Integer customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ReturnObject<Page<ModelByIdDTO>> result =
                customerModelService.getCustomerModelHistory(customerId, page, size);

        if (!result.getStatus()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }

        return ResponseEntity.ok(result);
    }

}

