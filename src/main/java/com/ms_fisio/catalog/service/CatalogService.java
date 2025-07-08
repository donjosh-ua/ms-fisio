package com.ms_fisio.catalog.service;

import com.ms_fisio.catalog.dto.TargetAreaDto;
import com.ms_fisio.catalog.dto.TargetAreasResponse;
import com.ms_fisio.exercise.domain.model.ObjectiveAreaModel;
import com.ms_fisio.exercise.repository.ObjectiveAreaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for catalog operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {
    
    private final ObjectiveAreaRepository objectiveAreaRepository;
    
    /**
     * Get all target areas from catalog
     */
    public TargetAreasResponse getTargetAreas() {
        log.info("Fetching all target areas from catalog");
        
        List<ObjectiveAreaModel> areas = objectiveAreaRepository.findAll();
        
        List<TargetAreaDto> targetAreas = areas.stream()
                .map(area -> new TargetAreaDto(area.getObjectiveAreaId(), area.getName()))
                .toList();
        
        return new TargetAreasResponse(targetAreas);
    }
}
