# Development Mode - JWT Authentication Disabled

## 🔧 Changes Made

For development and testing purposes, JWT authentication has been **temporarily disabled** across the entire application.

### Security Configuration Changes

**File:** `src/main/java/com/ms_fisio/auth/config/SecurityConfig.java`

- ✅ **All endpoints are now publicly accessible** (`.anyRequest().permitAll()`)
- ✅ **JWT filter has been removed** from the security chain
- ✅ **CORS is still enabled** for cross-origin requests
- ✅ **CSRF remains disabled** (appropriate for REST APIs)

### Controller Updates

**Account Controller & Dashboard Controller:**

- ✅ **Authorization headers are now optional** (`required = false`)
- ✅ **Mock user ID fallback** (defaults to user ID = 1 for testing)
- ✅ **Graceful degradation** - still works with JWT tokens if provided

### Test Files Updated

**rest-auth.http, rest-account.http, rest-dashboard.http:**

- ✅ **Authorization headers removed** from test requests
- ✅ **Port updated to 8081** to match your configuration
- ✅ **All endpoints can be tested without authentication**

## 🧪 Testing Instructions

### 1. Start the Application

```bash
./mvnw spring-boot:run
```

### 2. Test Endpoints Without Authentication

**Auth Endpoints:**

```http
GET http://localhost:8081/api/auth/health
POST http://localhost:8081/api/auth/login
```

**Account Endpoints:**

```http
PATCH http://localhost:8081/api/account
POST http://localhost:8081/api/account/change-password
DELETE http://localhost:8081/api/account
```

**Dashboard Endpoints:**

```http
GET http://localhost:8081/api/dashboard/info
POST http://localhost:8081/api/dashboard/charts
```

### 3. Test Data Available

**Test User (created automatically):**

- Email: `test@example.com`
- Password: `password123`
- User ID: 1 (used as default for account/dashboard operations)

## ⚠️ Important Notes

### For Development Only

This configuration is **only for development and testing**. In production:

- Re-enable JWT authentication
- Restore the security filter chain
- Make Authorization headers required again

### Re-enabling Authentication

To restore authentication for production:

1. **Uncomment JWT filter** in `SecurityConfig.java`:

   ```java
   .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
   ```

2. **Restore endpoint security**:

   ```java
   .authorizeHttpRequests(authz -> authz
       .requestMatchers("/api/auth/**").permitAll()
       .anyRequest().authenticated()
   )
   ```

3. **Make Authorization headers required** in controllers:
   ```java
   @RequestHeader("Authorization") String authorizationHeader
   ```

## 🚀 Current Status

- ✅ **No 403 errors** - all endpoints are accessible
- ✅ **All services can be tested** independently
- ✅ **Mock data available** for development
- ✅ **CORS properly configured** for cross-origin testing

You can now test all your endpoints without worrying about JWT tokens or authentication issues!
