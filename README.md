# ms-fisio

This is a microservice for managing physiotherapy patients, built with Java and Spring Boot. It provides a RESTful API for CRUD operations on patient data, including personal information, medical history, and treatment plans with JWT authentication and Google OAuth support.

## Quick Start with Docker

The easiest way to run the application is using Docker:

```bash
# Clone the repository
git clone <repository-url>
cd ms-fisio

# Start the application
docker-compose up --build -d

# Verify everything is running
# Windows:
.\scripts\validate.ps1
# Linux/Mac:
./scripts/validate.sh
```

**Access the application:**

- Application: http://localhost:8080
- Health Check: http://localhost:8080/actuator/health
- Database Admin: http://localhost:5050 (start with `--profile dev`)

**Test Credentials:**

- Email: `test@example.com`
- Password: `password123`

For detailed Docker setup instructions, see [DOCKER-SETUP.md](DOCKER-SETUP.md)

## Architecture

```
src/main/java/com/ms_fisio/
├── user/                           # User Management Module
│   ├── domain/
│   │   ├── model/
│   │   │   ├── UserModel.java
│   │   │   └── NotificationModel.java
│   │   └── dto/
│   │       ├── UserDTO.java
│   │       ├── CreateUserRequest.java
│   │       ├── UserResponseDTO.java
│   │       └── NotificationDTO.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   └── NotificationRepository.java
│   ├── service/
│   │   ├── UserService.java
│   │   └── NotificationService.java
│   ├── controller/
│   │   ├── UserController.java
│   │   └── NotificationController.java
│   └── mapper/
│       └── UserMapper.java
│
├── subscription/                   # Subscription & Billing Module
│   ├── domain/
│   │   ├── model/
│   │   │   ├── PlanTypeModel.java
│   │   │   └── SubscriptionModel.java
│   │   └── dto/
│   │       ├── PlanTypeDTO.java
│   │       ├── SubscriptionDTO.java
│   │       └── CreateSubscriptionRequest.java
│   ├── repository/
│   │   ├── PlanTypeRepository.java
│   │   └── SubscriptionRepository.java
│   ├── service/
│   │   ├── PlanTypeService.java
│   │   └── SubscriptionService.java
│   ├── controller/
│   │   └── SubscriptionController.java
│   └── mapper/
│       └── SubscriptionMapper.java
│
├── exercise/                       # Exercise Management Module
│   ├── domain/
│   │   ├── model/
│   │   │   ├── ExerciseModel.java
│   │   │   ├── ObjectiveAreaModel.java
│   │   │   └── ExerciseMomentModel.java
│   │   └── dto/
│   │       ├── ExerciseDTO.java
│   │       ├── CreateExerciseRequest.java
│   │       ├── ObjectiveAreaDTO.java
│   │       └── ExerciseMomentDTO.java
│   ├── repository/
│   │   ├── ExerciseRepository.java
│   │   ├── ObjectiveAreaRepository.java
│   │   └── ExerciseMomentRepository.java
│   ├── service/
│   │   ├── ExerciseService.java
│   │   └── ObjectiveAreaService.java
│   ├── controller/
│   │   └── ExerciseController.java
│   └── mapper/
│       └── ExerciseMapper.java
│
├── routine/                        # Routine Management Module
│   ├── domain/
│   │   ├── model/
│   │   │   ├── RoutineModel.java
│   │   │   ├── RoutineDayModel.java
│   │   │   └── RoutineExerciseModel.java
│   │   └── dto/
│   │       ├── RoutineDTO.java
│   │       ├── CreateRoutineRequest.java
│   │       ├── RoutineDetailsDTO.java
│   │       └── RoutineScheduleDTO.java
│   ├── repository/
│   │   ├── RoutineRepository.java
│   │   ├── RoutineDayRepository.java
│   │   └── RoutineExerciseRepository.java
│   ├── service/
│   │   ├── RoutineService.java
│   │   └── RoutineScheduleService.java
│   ├── controller/
│   │   └── RoutineController.java
│   └── mapper/
│       └── RoutineMapper.java
│
├── session/                        # Session & Performance Module
│   ├── domain/
│   │   ├── model/
│   │   │   ├── RoutineSessionModel.java
│   │   │   ├── ExercisePerformanceModel.java
│   │   │   └── FeedbackModel.java
│   │   └── dto/
│   │       ├── SessionDTO.java
│   │       ├── StartSessionRequest.java
│   │       ├── PerformanceDTO.java
│   │       ├── PerformanceAnalysisDTO.java
│   │       └── FeedbackDTO.java
│   ├── repository/
│   │   ├── RoutineSessionRepository.java
│   │   ├── ExercisePerformanceRepository.java
│   │   └── FeedbackRepository.java
│   ├── service/
│   │   ├── SessionService.java
│   │   ├── PerformanceAnalysisService.java
│   │   └── FeedbackService.java
│   ├── controller/
│   │   ├── SessionController.java
│   │   └── PerformanceController.java
│   └── mapper/
│       ├── SessionMapper.java
│       └── PerformanceMapper.java
│
├── patient/                        # Patient Management Module
│   ├── domain/
│   │   ├── model/
│   │   │   ├── PatientProfileModel.java
│   │   │   ├── ChronicDiseaseModel.java
│   │   │   ├── AffectedZoneModel.java
│   │   │   ├── LesionTypeModel.java
│   │   │   ├── PatientDiseaseModel.java
│   │   │   ├── PatientZoneModel.java
│   │   │   ├── PatientLesionModel.java
│   │   │   └── PatientRoutineModel.java
│   │   └── dto/
│   │       ├── PatientProfileDTO.java
│   │       ├── CreatePatientRequest.java
│   │       ├── PatientMedicalHistoryDTO.java
│   │       ├── ChronicDiseaseDTO.java
│   │       ├── AffectedZoneDTO.java
│   │       └── LesionTypeDTO.java
│   ├── repository/
│   │   ├── PatientProfileRepository.java
│   │   ├── ChronicDiseaseRepository.java
│   │   ├── AffectedZoneRepository.java
│   │   ├── LesionTypeRepository.java
│   │   └── PatientMedicalHistoryRepository.java
│   ├── service/
│   │   ├── PatientProfileService.java
│   │   ├── PatientMedicalHistoryService.java
│   │   └── MedicalDataService.java
│   ├── controller/
│   │   ├── PatientController.java
│   │   └── MedicalDataController.java
│   └── mapper/
│       └── PatientMapper.java
│
├── shared/                         # Shared Components
│   ├── domain/
│   │   ├── enums/
│   │   │   ├── DifficultyLevel.java
│   │   │   ├── DurationUnit.java
│   │   │   ├── DayOfWeek.java
│   │   │   └── ComplianceLevel.java
│   │   ├── exception/
│   │   │   ├── BusinessException.java
│   │   │   ├── EntityNotFoundException.java
│   │   │   └── ValidationException.java
│   │   └── dto/
│   │       ├── BaseResponse.java
│   │       ├── PageResponse.java
│   │       └── ErrorResponse.java
│   ├── config/
│   │   ├── DatabaseConfig.java
│   │   ├── SecurityConfig.java
│   │   ├── WebConfig.java
│   │   └── JpaConfig.java
│   ├── util/
│   │   ├── DateUtil.java
│   │   ├── ValidationUtil.java
│   │   └── MapperUtil.java
│   └── security/
│       ├── JwtUtil.java
│       ├── AuthenticationService.java
│       └── SecurityConstants.java
│
└── Application.java                # Main application class
```
