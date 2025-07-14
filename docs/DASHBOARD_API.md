# Dashboard API

This module provides dashboard functionality for authenticated users, including notifications and chart analytics.

## Endpoints

### 1. Get Dashboard Information

**GET** `/api/dashboard/info`

Fetches all necessary information to display on the dashboard including notifications and ongoing sessions.

**Request Headers:**

- `Authorization: Bearer <jwt_token>`

**Response:**

```json
{
  "notifications": [
    {
      "originName": "Sistema",
      "sentAt": "2025-07-07T12:30:00Z",
      "content": "Tu rutina ha sido actualizada."
    },
    {
      "originName": "Dr. García",
      "sentAt": "2025-07-07T10:30:00Z",
      "content": "Recuerda completar tu sesión de hoy."
    }
  ],
  "ongoingSessions": [
    {
      "chuecoName": "Luis",
      "chuecoPfpUrl": "https://example.com/images/luis.jpg",
      "routineName": "Piernas Hardcore",
      "routineId": "abc123"
    },
    {
      "chuecoName": "María",
      "chuecoPfpUrl": "https://example.com/images/maria.jpg",
      "routineName": "Brazos Intensivo",
      "routineId": "def456"
    }
  ]
}
```

### 2. Request Analysis Charts

**POST** `/api/dashboard/charts`

Receives a preferred chart type and returns relevant chart images with metadata.

**Request Headers:**

- `Authorization: Bearer <jwt_token>`

**Request Body:**

```json
{
  "chartType": "barras" // "barras" or "lineas"
}
```

**Response for "barras" type:**

```json
{
  "charts": [
    {
      "type": "barras",
      "source": "https://example.com/charts/barras_weekly.png"
    },
    {
      "type": "barras",
      "source": "https://example.com/charts/barras_monthly.png"
    }
  ]
}
```

**Response for "lineas" type:**

```json
{
  "charts": [
    {
      "type": "completadas",
      "source": "https://example.com/charts/completadas_2024.png",
      "year": 2024
    },
    {
      "type": "planificadas",
      "source": "https://example.com/charts/planificadas_2024.png",
      "year": 2024
    },
    {
      "type": "completadas",
      "source": "https://example.com/charts/completadas_2023.png",
      "year": 2023
    }
  ]
}
```

## Data Models

### Notification

- `originName`: Source of the notification (e.g., "Sistema", "Dr. García")
- `sentAt`: Timestamp in ISO 8601 format
- `content`: Notification message

### Ongoing Session

- `chuecoName`: Name of the person in session
- `chuecoPfpUrl`: Profile picture URL
- `routineName`: Name of the routine being performed
- `routineId`: Unique identifier for the routine

### Chart

- `type`: Chart category ("barras", "completadas", "planificadas", etc.)
- `source`: URL to the chart image
- `year`: Year of the data (only present for non-"barras" charts)

## Validation Rules

### Chart Type

- Must be either "barras" or "lineas"
- Required field

## Features

### Chart Logic

- **"barras" charts**: Do not include year field, represent weekly/monthly summaries
- **"lineas" charts**: Include year field, show historical data with different types

### Mock Data

- The service provides realistic mock data for development and testing
- Notifications include system and doctor messages with proper timestamps
- Ongoing sessions show different users with various routines

## Authentication

All endpoints require a valid JWT token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

The user ID is extracted from the JWT token to personalize the dashboard content.

## Error Handling

- **400 Bad Request**: Invalid chart type or missing required fields
- **401 Unauthorized**: Missing or invalid JWT token
- Standard validation error responses for malformed requests
