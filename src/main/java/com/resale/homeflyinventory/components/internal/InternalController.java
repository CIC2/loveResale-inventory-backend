package com.resale.homeflyinventory.components.internal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/internal")
public class InternalController {

    @Autowired
    private InternalService internalService;

    @GetMapping("/{modelId}")
    public ResponseEntity<?> getModelProject(@PathVariable Integer modelId) {
        var result = internalService.getModelProjectDetails(modelId);

        if (result.getStatus()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
}


