package com.ms_fisio.patient.test;

import com.ms_fisio.patient.domain.dto.ChronicDiseaseDTO;
import com.ms_fisio.patient.domain.dto.AffectedZoneDTO;
import com.ms_fisio.patient.domain.dto.LesionTypeDTO;

/**
 * Test class to verify DTO compilation
 */
public class DTOTest {
    
    public void testDTOs() {
        ChronicDiseaseDTO disease = new ChronicDiseaseDTO(1L, "Test Disease");
        AffectedZoneDTO zone = new AffectedZoneDTO(1L, "Test Zone");
        LesionTypeDTO lesion = new LesionTypeDTO(1L, "Test Lesion");
        
        System.out.println("DTOs working: " + disease.getName() + ", " + zone.getName() + ", " + lesion.getName());
    }
}
