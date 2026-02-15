package com.resale.homeflyinventory.components.model;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.resale.homeflyinventory.components.model.dto.ModelDTO;
import com.resale.homeflyinventory.components.model.dto.ModelDetailsDTO;
import com.resale.homeflyinventory.components.model.dto.ModelResponseDTO;
import com.resale.homeflyinventory.logging.LogActivity;
import com.resale.homeflyinventory.models.ActionType;
import com.resale.homeflyinventory.models.Model;
import com.resale.homeflyinventory.security.CheckPermission;
import com.resale.homeflyinventory.utils.ReturnObject;

import java.util.List;


@RestController
@RequestMapping("/sales/model")
@RequiredArgsConstructor
public class SalesModelController {

    @Autowired
    SalesModelService salesModelService;


    @GetMapping("/{modelId}/details")
    @CheckPermission(value = {"admin:login"})
    @LogActivity(ActionType.GET_MODEL_DETAILS)
    public ResponseEntity<ReturnObject<ModelDetailsDTO>> getModelDetails(@PathVariable Integer modelId) {
        ReturnObject<ModelDetailsDTO> response = salesModelService.getModelDetails(modelId);

        if (!response.getStatus()) {
            if (response.getData() == null && response.getMessage().toLowerCase().contains("invalid")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    @CheckPermission(value = {"admin:login"})
    @LogActivity(ActionType.GET_MODELS)
    public ResponseEntity<ReturnObject<List<ModelDTO>>> getAllModels() {
        ReturnObject<List<ModelDTO>> result = salesModelService.getAllModels();
        return result.getStatus()
                ? ResponseEntity.ok(result)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }


    @PutMapping("/{id}")
    @CheckPermission(value = {"admin:login"})
    @LogActivity(ActionType.UPDATE_MODEL)
    public ResponseEntity<ReturnObject<ModelResponseDTO>> updateModel(
            @PathVariable Integer id,
            @RequestBody Model updatedModel) {

        ReturnObject<ModelResponseDTO> result = salesModelService.updateModel(id, updatedModel);
        if (!result.getStatus()) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);

    }

}


