# Session Feedback & Exercise Performance Endpoints

## 🎯 **New Features Added**

### **1. Feedback Sender Endpoint**

Allows users to send feedback for a session using the access code.

#### **Endpoint**

```http
POST /api/session/feedback
```

#### **Request Body**

```json
{
  "accessCode": "PAT-A1B2C3D4",
  "feedback": "The exercises were very helpful. I felt improvement in my knee mobility.",
  "calification": 5
}
```

#### **Response**

```json
{
  "success": true,
  "message": "Feedback sent successfully",
  "session": null
}
```

#### **Features**

- ✅ **Access Code Lookup** - Finds session by access code first
- ✅ **No Expiration Check** - Allows feedback even for expired sessions
- ✅ **Validation** - Validates access code and feedback content
- ✅ **Rating System** - Includes optional calification (rating/score) field
- ✅ **Database Storage** - Saves feedback to `feedbacks` table

---

### **2. Exercise Performance Endpoint**

Retrieves exercise performance data for a session (will connect to external backend).

#### **Endpoint**

```http
GET /api/session/performance/{accessCode}
```

#### **Response**

```json
[
  {
    "exercisePerformanceId": 1,
    "postureCompliance": 85.5,
    "repetitionCompliance": 92.0,
    "imageCaptures": "capture1.jpg,capture2.jpg",
    "complianceLevel": "GOOD",
    "exerciseId": 101,
    "exerciseName": "Knee Flexion Exercise",
    "sessionId": 123
  }
]
```

#### **Features**

- ✅ **Access Code Lookup** - Finds session by access code
- ✅ **Mock Data** - Currently returns sample performance data
- ✅ **External API Ready** - Configured for future external backend integration
- ✅ **Performance Metrics** - Posture and repetition compliance tracking

---

## 🔧 **Implementation Details**

### **DTOs Created**

1. **FeedbackDTO** - For feedback data structure
2. **SendFeedbackRequest** - For feedback submission
3. **ExercisePerformanceDTO** - For exercise performance data

### **Service Methods Added**

1. **`sendFeedback()`** - Processes feedback submission
2. **`getExercisePerformance()`** - Retrieves performance data
3. **`createMockExercisePerformance()`** - Generates sample data

### **Controller Endpoints Added**

1. **`POST /session/feedback`** - Feedback submission
2. **`GET /session/performance/{accessCode}`** - Performance data retrieval

### **Configuration Added**

```properties
# External Services Configuration
app.external.exercise-analysis.url=${EXERCISE_ANALYSIS_URL:http://localhost:8082/api/exercise-analysis}
```

---

## 🌐 **External Backend Integration**

### **Current State**

- ✅ **Mock Implementation** - Returns sample exercise performance data
- ✅ **Configuration Ready** - External URL configured in properties
- ✅ **RestTemplate Setup** - Ready for HTTP calls

### **Future Integration**

When the external exercise analysis backend is ready:

1. **Uncomment API Call Code**:

```java
String url = exerciseAnalysisUrl + "/performance/" + accessCode;
ExercisePerformanceDTO[] response = restTemplate.getForObject(url, ExercisePerformanceDTO[].class);
return Arrays.asList(response);
```

2. **Remove Mock Data**:

```java
// Remove createMockExercisePerformance() call
```

3. **Configure External URL**:

```properties
EXERCISE_ANALYSIS_URL=http://your-external-backend:8082/api/exercise-analysis
```

---

## 📊 **Data Flow**

### **Feedback Flow**

1. Patient completes session
2. Patient uses access code to submit feedback
3. System finds session by access code
4. Feedback saved to database
5. Confirmation returned

### **Exercise Performance Flow**

1. Patient/Physiotherapist requests performance data
2. System finds session by access code
3. **Current**: Return mock performance data
4. **Future**: Call external backend for analysis
5. Return performance metrics and compliance data

---

## 🧪 **Testing**

### **Feedback Testing**

```http
POST http://localhost:8081/api/session/feedback
Content-Type: application/json

{
  "accessCode": "PAT-A1B2C3D4",
  "feedback": "Excellent session, felt improvement!",
  "calification": 5
}
```

### **Performance Testing**

```http
GET http://localhost:8081/api/session/performance/PAT-A1B2C3D4
```

---

## ✅ **Implementation Status**

- ✅ **Feedback Endpoint** - Fully functional
- ✅ **Performance Endpoint** - Mock data working
- ✅ **Database Integration** - Feedback storage working
- ✅ **Validation** - Input validation implemented
- ✅ **Error Handling** - Comprehensive error responses
- ✅ **Documentation** - HTTP test cases provided
- ✅ **Configuration** - External service URL ready
- 🔄 **External Integration** - Ready for future backend connection

The session module now supports complete patient interaction including feedback submission and performance monitoring!
