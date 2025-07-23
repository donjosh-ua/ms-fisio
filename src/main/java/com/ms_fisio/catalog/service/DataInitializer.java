package com.ms_fisio.catalog.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ms_fisio.exercise.domain.model.ObjectiveAreaModel;
import com.ms_fisio.exercise.repository.ObjectiveAreaRepository;
import com.ms_fisio.patient.domain.model.AffectedZoneModel;
import com.ms_fisio.patient.domain.model.ChronicDiseaseModel;
import com.ms_fisio.patient.domain.model.LesionTypeModel;
import com.ms_fisio.patient.repository.AffectedZoneRepository;
import com.ms_fisio.patient.repository.ChronicDiseaseRepository;
import com.ms_fisio.patient.repository.LesionTypeRepository;
import com.ms_fisio.shared.domain.enums.AuthProvider;
import com.ms_fisio.subscription.domain.model.PlanTypeModel;
import com.ms_fisio.subscription.repository.PlanTypeRepository;
import com.ms_fisio.user.domain.model.UserModel;
import com.ms_fisio.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ObjectiveAreaRepository objectiveAreaRepository;
    private final PasswordEncoder passwordEncoder;
    private final PlanTypeRepository planTypeRepository;
    private final AffectedZoneRepository affectedZoneRepository;
    private final LesionTypeRepository lesionTypeRepository;
    private final ChronicDiseaseRepository chronicDiseaseRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeCatalogs();
    }

    private void initializeCatalogs() {
        initializeAffectedZones();
        initializeLessionTypes();
        initializeChronicDiseases();
        initializeObjectiveAreas();
        initializePlanTypes();
        initializeUsers();
        log.info("Catalogs initialized successfully.");
    }

    private void initializeUsers() {
        if (!userRepository.existsByEmail("test@example.com")) {

            UserModel testUser = new UserModel();

            testUser.setFullName("Test User");
            testUser.setUsername("testuser");
            testUser.setEmail("test@example.com");
            testUser.setPassword(passwordEncoder.encode("password123"));
            testUser.setAuthProvider(AuthProvider.LOCAL);
            testUser.setEmailVerified(true);

            userRepository.save(testUser);

            log.info("Created test user: test@example.com / password123");
        }
    }

    private void initializeObjectiveAreas() {

        if (objectiveAreaRepository.count() == 0) {

            ObjectiveAreaModel piernas = new ObjectiveAreaModel();
            piernas.setName("Piernas");
            objectiveAreaRepository.save(piernas);

            ObjectiveAreaModel espalda = new ObjectiveAreaModel();
            espalda.setName("Espalda");
            objectiveAreaRepository.save(espalda);

            ObjectiveAreaModel brazos = new ObjectiveAreaModel();
            brazos.setName("Brazos");
            objectiveAreaRepository.save(brazos);

            ObjectiveAreaModel core = new ObjectiveAreaModel();
            core.setName("Core");
            objectiveAreaRepository.save(core);

            log.info("Created target areas for catalog");
        }
    }

    private void initializePlanTypes() {
    if (planTypeRepository.count() == 0) {
        planTypeRepository.save(PlanTypeModel.builder()
                .name("Basic Plan Mensual")
                .price(4.99)
                .build());
        planTypeRepository.save(PlanTypeModel.builder()
                .name("Premium Plan Mensual")
                .price(9.99)
                .build());
        planTypeRepository.save(PlanTypeModel.builder()
                .name("Basic Plan Anual")
                .price(49.99)
                .build());
        planTypeRepository.save(PlanTypeModel.builder()
                .name("Premium Plan Anual")
                .price(89.99)
                .build());
        log.info("Created plan types for catalog");
    }
}

    private void initializeAffectedZones() {
        if (affectedZoneRepository.count() == 0) {
            affectedZoneRepository.save(AffectedZoneModel.builder()
                    .name("Pierna")
                    .build());
            affectedZoneRepository.save(AffectedZoneModel.builder()
                    .name("Espalda")
                    .build());
            affectedZoneRepository.save(AffectedZoneModel.builder()
                    .name("Hombro")
                    .build());
            log.info("Created affected zones for catalog");
        }
    }

    private void initializeLessionTypes() {
        if (lesionTypeRepository.count() == 0) {
            lesionTypeRepository.save(LesionTypeModel.builder()
                    .name("Fractura")
                    .build());
            lesionTypeRepository.save(LesionTypeModel.builder()
                    .name("Esguince")
                    .build());
            lesionTypeRepository.save(LesionTypeModel.builder()
                    .name("Desgarro")
                    .build());
            log.info("Created lesion types for catalog");
        }
    }

    private void initializeChronicDiseases() {
        if (chronicDiseaseRepository.count() == 0) {
            chronicDiseaseRepository.save(ChronicDiseaseModel.builder()
                    .name("Diabetes")
                    .build());
            chronicDiseaseRepository.save(ChronicDiseaseModel.builder()
                    .name("Hipertensión")
                    .build());
            chronicDiseaseRepository.save(ChronicDiseaseModel.builder()
                    .name("Artritis")
                    .build());
            log.info("Created chronic diseases for catalog");
        }
    }
}
