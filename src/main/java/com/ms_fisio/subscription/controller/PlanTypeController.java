package com.ms_fisio.subscription.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms_fisio.subscription.domain.dto.PlanTypeDTO;
import com.ms_fisio.subscription.domain.model.PlanTypeModel;
import com.ms_fisio.subscription.repository.PlanTypeRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/plan-type")
@RequiredArgsConstructor
public class PlanTypeController {

    private final PlanTypeRepository planTypeRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody PlanTypeDTO dto) {
        if (planTypeRepository.existsById(dto.getPlanTypeId())) {
            return ResponseEntity.badRequest().body("El plan con ese ID ya existe.");
        }
        if (dto.getPlanTypeId() < 1 || dto.getPlanTypeId() > 4) {
            return ResponseEntity.badRequest().body("Solo se permiten IDs del 1 al 4 para los planes predefinidos.");
        }
        PlanTypeModel plan = new PlanTypeModel();
        plan.setPlanTypeId(dto.getPlanTypeId());
        plan.setName(dto.getName());
        plan.setPrice(dto.getPrice());
        return ResponseEntity.ok(planTypeRepository.save(plan));
    }
}