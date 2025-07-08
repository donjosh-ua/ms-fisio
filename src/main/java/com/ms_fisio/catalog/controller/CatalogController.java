package com.ms_fisio.catalog.controller;

import com.ms_fisio.catalog.dto.TargetAreasResponse;
import com.ms_fisio.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for catalog endpoints
 */
@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
@Slf4j
public class CatalogController {
    
    private final CatalogService catalogService;
    
    /**
     * Get catalog of target areas
     */
    @GetMapping("/target-areas")
    public ResponseEntity<TargetAreasResponse> getTargetAreas() {
        log.info("Request to get target areas catalog");
        
        TargetAreasResponse response = catalogService.getTargetAreas();
        return ResponseEntity.ok(response);
    }
}
