# Account Settings API

This module provides account management functionality for authenticated users.

## Endpoints

### 1. Update Account Information

**PATCH** `/api/account`

Updates user account information. Only provided fields will be updated.

**Request Headers:**

- `Authorization: Bearer <jwt_token>`

**Request Body:**
These fields are optional and can be updated individually:

```json
{
  "username": "new_username",
  "fullName": "new_full_name",
  "email": "new_email@example.com"
}
```

**Response:**

```json
{
  "success": true,
  "message": "Account information updated successfully."
}
```

**Error Response:**

```json
{
  "success": false,
  "error": "Username already exists."
}
```

### 2. Change Password

**POST** `/api/account/change-password`

Changes the user's password after verifying the current password.

**Request Headers:**

- `Authorization: Bearer <jwt_token>`

**Request Body:**

```json
{
  "currentPassword": "oldPass123",
  "newPassword": "newPass456"
}
```

**Response:**

```json
{
  "success": true,
  "message": "Password changed successfully."
}
```

**Error Response:**

```json
{
  "success": false,
  "error": "Current password is incorrect."
}
```

### 3. Delete Account

**DELETE** `/api/account`

Permanently deletes the user's account.

**Request Headers:**

- `Authorization: Bearer <jwt_token>`

**Response:**

```json
{
  "success": true,
  "message": "Account deleted successfully."
}
```

**Error Response:**

```json
{
  "success": false,
  "error": "Unable to delete account at this time."
}
```

## Validation Rules

### Username

- 3-50 characters
- Must be unique

### Full Name

- 2-100 characters

### Email

- Valid email format
- Must be unique

### Password

- Minimum 6 characters
- Must be different from current password

## Error Handling

The API returns consistent error responses with the following structure:

- `success`: boolean indicating operation success
- `message`: success message (when success is true)
- `error`: error description (when success is false)

## Authentication

All endpoints require a valid JWT token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

The user ID is extracted from the JWT token to identify which account to modify.
