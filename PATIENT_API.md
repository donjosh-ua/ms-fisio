# Patient API Documentation

This module provides endpoints for managing patient profiles, accessing medical catalogs, and handling physiotherapy routines.

## Directory Structure

```
src/main/java/com/ms_fisio/patient/
├── controller/
│   └── PatientController.java        # REST endpoints
├── service/
│   └── PatientService.java          # Business logic
├── domain/
│   ├── dto/                         # Data Transfer Objects
│   │   ├── ChronicDiseaseDTO.java
│   │   ├── AffectedZoneDTO.java
│   │   ├── LesionTypeDTO.java
│   │   ├── PatientProfileRequest.java
│   │   ├── PatientProfileDTO.java
│   │   ├── CreatePatientProfileRequest.java
│   │   ├── UpdatePatientProfileRequest.java
│   │   ├── DeleteRoutineRequest.java
│   │   ├── PatientDiseaseDTO.java
│   │   ├── PatientZoneDTO.java
│   │   ├── PatientLesionDTO.java
│   │   ├── PatientRoutineDTO.java
│   │   └── ApiResponse.java
│   └── model/                       # JPA Entities (already existed)
├── repository/                      # Data access layer (already existed)
└── exception/
    └── UnauthorizedAccessException.java
```

## API Endpoints

### Catalog Endpoints

- `GET /api/patient/catalog/chronic-diseases` - Get all chronic diseases
- `GET /api/patient/catalog/affected-zones` - Get all affected body zones
- `GET /api/patient/catalog/lesion-types` - Get all lesion types

### Routine Management

- `GET /api/patient/routines` - Get physiotherapist routines (requires auth)

### Patient Profile Management

- `POST /api/patient/profile` - Create or update patient profile (requires auth)
  - **⚡ NEW: Creates session ID and access code for new profiles**
  - Updates to existing profiles do not create new sessions
- `POST /api/patient/routine/delete` - Delete routine assignment (requires auth)

## Authentication

All endpoints except catalog endpoints require JWT authentication via the `Authorization` header:

```
Authorization: Bearer <jwt-token>
```

User ID is extracted from the JWT token using the same pattern as the dashboard controller.

## Features

### 1. Medical Catalogs

- **Chronic Diseases**: Diabetes, hypertension, etc.
- **Affected Zones**: Knee, shoulder, back, etc.
- **Lesion Types**: Sprain, fracture, tear, etc.

### 2. Patient Profile Management

- Create/update comprehensive patient profiles
- Associate multiple chronic diseases, affected zones, and lesion types
- Track medical history, surgeries, pain levels, and diagnoses
- Validation for all input fields
- **⚡ NEW: Automatic session creation for new profiles**

### 3. Session Management (NEW)

- When a new patient profile is created, a session is automatically generated
- Session includes:
  - Unique session ID
  - Access code (format: "PAT-XXXXXXXX")
  - Start timestamp
- Updates to existing profiles do not create new sessions

### 4. Routine Management

- Get available physiotherapy routines
- Delete routine assignments
- User authorization checks

## Data Validation

The `PatientProfileRequest` includes comprehensive validation:

- Required fields: fullName, idNumber
- Email format validation
- Age range: 1-150
- Pain level range: 0-10
- String length limits for all text fields

## Error Handling

- Custom `UnauthorizedAccessException` for security violations
- Comprehensive error messages in `ApiResponse`
- Transaction rollback on failures

## Usage Examples

See `patient-api.http` for complete request examples.

### Create Patient Profile

```json
{
  "fullName": "Juan Pérez",
  "idNumber": "123456789",
  "phone": "+50687654321",
  "age": 35,
  "sex": "M",
  "email": "juan.perez@example.com",
  "priorSurgeries": "Knee surgery in 2020",
  "chronicDiseaseIds": [1, 2],
  "affectedZoneIds": [1, 3],
  "lesionTypeIds": [2],
  "painStartDate": "2024-01-15",
  "painLevel": 7,
  "medicalDiagnosis": "ACL injury"
}
```

### Delete Routine Assignment

```json
{
  "patientProfileId": 1,
  "routineSessionId": 123
}
```

## Response Formats

### Patient Profile Creation Response (NEW)

When creating a new patient profile, the response includes session information:

```json
{
  "success": true,
  "message": "Patient profile saved successfully",
  "patientProfileId": 1,
  "sessionId": 123,
  "accessCode": "PAT-A1B2C3D4"
}
```

### Patient Profile Update Response

When updating an existing profile, no session is created:

```json
{
  "success": true,
  "message": "Patient profile saved successfully",
  "patientProfileId": 1,
  "sessionId": null,
  "accessCode": null
}
```

### Error Response

```json
{
  "success": false,
  "message": "Error saving patient profile: [error details]",
  "patientProfileId": null,
  "sessionId": null,
  "accessCode": null
}
```
