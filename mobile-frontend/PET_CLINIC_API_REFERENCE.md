# Pet Clinic API Reference

This document provides a comprehensive overview of the Pet Clinic microservices API endpoints available through the API Gateway.

## Architecture Overview

The Pet Clinic application follows a microservices architecture with the following services:

- **API Gateway** (`spring-petclinic-api-gateway`) - Routes requests to appropriate services
- **Customers Service** (`spring-petclinic-customers-service`) - Manages owners and pets
- **Visits Service** (`spring-petclinic-visits-service`) - Manages veterinary visits
- **Vets Service** (`spring-petclinic-vets-service`) - Manages veterinarians
- **Insurance Service** (`pet_clinic_insurance_service`) - Manages pet insurance
- **Billing Service** (`pet_clinic_billing_service`) - Manages billing information
- **Payment Service** (`dotnet-petclinic-payment`) - Handles payments (.NET service)
- **Nutrition Service** (`pet-nutrition-service`) - Provides pet nutrition information
- **Config Server** (`spring-petclinic-config-server`) - Centralized configuration
- **Discovery Server** (`spring-petclinic-discovery-server`) - Service discovery (Eureka)

## Base URL

All API endpoints are accessed through the API Gateway:
- Local Development: `http://localhost:8080/api/`
- Production: `http://{api-gateway-host}/api/`

## API Endpoints

### 1. Owner Management

#### Get All Owners
```http
GET /api/customer/owners
```
**Response:** Array of `OwnerDetails`

#### Get Owner by ID
```http
GET /api/customer/owners/{ownerId}
```
**Parameters:**
- `ownerId` (path) - Owner ID

**Response:** `OwnerDetails` object

#### Update Owner
```http
PUT /api/customer/owners/{ownerId}
```
**Parameters:**
- `ownerId` (path) - Owner ID
- Request Body: `OwnerRequest`

#### Add New Owner
```http
POST /api/customer/owners
```
**Request Body:** `OwnerRequest`

#### Get Owner Details with Visits (Gateway Endpoint)
```http
GET /api/gateway/owners/{ownerId}
```
**Parameters:**
- `ownerId` (path) - Owner ID

**Response:** `OwnerDetails` with populated visits
**Features:** Circuit breaker protection, combines data from customers and visits services

### 2. Pet Management

#### Get Pet Types
```http
GET /api/customer/petTypes
```
**Response:** Array of `PetType`

#### Get Pet Details
```http
GET /api/customer/owners/{ownerId}/pets/{petId}
```
**Parameters:**
- `ownerId` (path) - Owner ID
- `petId` (path) - Pet ID

**Response:** `PetFull` object

#### Update Pet
```http
PUT /api/customer/owners/{ownerId}/pets/{petId}
```
**Parameters:**
- `ownerId` (path) - Owner ID
- `petId` (path) - Pet ID
- Request Body: `PetRequest`

#### Add New Pet
```http
POST /api/customer/owners/{ownerId}/pets
```
**Parameters:**
- `ownerId` (path) - Owner ID
- Request Body: `PetRequest`

**Response:** `PetFull` object

#### Diagnose Pet
```http
GET /api/customer/diagnose/owners/{ownerId}/pets/{petId}
```
**Parameters:**
- `ownerId` (path) - Owner ID
- `petId` (path) - Pet ID

### 3. Veterinarian Management

#### Get All Veterinarians
```http
GET /api/vet/vets
```
**Response:** Array of `VetDetails`

### 4. Visit Management

#### Get Visits for Pet
```http
GET /api/visit/owners/{ownerId}/pets/{petId}/visits
```
**Parameters:**
- `ownerId` (path) - Owner ID
- `petId` (path) - Pet ID

**Response:** `Visits` object

#### Add Visit
```http
POST /api/visit/owners/{ownerId}/pets/{petId}/visits
```
**Parameters:**
- `ownerId` (path) - Owner ID
- `petId` (path) - Pet ID
- Request Body: `VisitDetails`

**Response:** Visit ID as string

### 5. Insurance Management

#### Get All Insurance Plans
```http
GET /api/insurance/insurances
```
**Response:** Array of `InsuranceDetail`

#### Add Pet Insurance
```http
POST /api/insurance/pet-insurances
```
**Request Body:** `PetInsurance`

#### Update Pet Insurance
```http
PUT /api/insurance/pet-insurances/{petId}
```
**Parameters:**
- `petId` (path) - Pet ID
- Request Body: `PetInsurance`

**Response:** `PetInsurance` object

#### Get Pet Insurance
```http
GET /api/insurance/pet-insurances/{petId}
```
**Parameters:**
- `petId` (path) - Pet ID

**Response:** `PetInsurance` object

### 6. Billing Management

#### Get All Billings
```http
GET /api/billing/billings
```
**Response:** Array of `BillingDetail`

### 7. Payment Management

#### Get Payments for Pet
```http
GET /api/payments/owners/{ownerId}/pets/{petId}
```
**Parameters:**
- `ownerId` (path) - Owner ID
- `petId` (path) - Pet ID

**Response:** Array of `PaymentDetail`

#### Get Payment by ID
```http
GET /api/payments/owners/{ownerId}/pets/{petId}/{paymentId}
```
**Parameters:**
- `ownerId` (path) - Owner ID
- `petId` (path) - Pet ID
- `paymentId` (path) - Payment ID

**Response:** `PaymentDetail` object

#### Add Payment
```http
POST /api/payments/owners/{ownerId}/pets/{petId}
```
**Parameters:**
- `ownerId` (path) - Owner ID
- `petId` (path) - Pet ID
- Request Body: `PaymentAdd`

**Response:** `PaymentDetail` object

#### Clean Payment Database (Development)
```http
DELETE /api/payments/clean-db
```
**Response:** `PaymentDetail` object

### 8. Nutrition Management

#### Get Pet Nutrition Information
```http
GET /api/nutrition/facts/{petType}
```
**Parameters:**
- `petType` (path) - Pet type (e.g., "dog", "cat")

**Response:** `PetNutrition` object

## Data Models

### OwnerDetails
```json
{
  "id": 1,
  "firstName": "George",
  "lastName": "Franklin",
  "address": "110 W. Liberty St.",
  "city": "Madison",
  "telephone": "6085551023",
  "pets": [
    {
      "id": 1,
      "name": "Leo",
      "birthDate": "2010-09-07",
      "type": {
        "id": 1,
        "name": "cat"
      },
      "visits": []
    }
  ]
}
```

### OwnerRequest
```json
{
  "firstName": "George",
  "lastName": "Franklin",
  "address": "110 W. Liberty St.",
  "city": "Madison",
  "telephone": "6085551023"
}
```

### PetDetails
```json
{
  "id": 1,
  "name": "Leo",
  "birthDate": "2010-09-07",
  "type": {
    "id": 1,
    "name": "cat"
  },
  "visits": []
}
```

### PetRequest
```json
{
  "name": "Leo",
  "birthDate": "2010-09-07",
  "typeId": 1
}
```

### PetType
```json
{
  "id": 1,
  "name": "cat"
}
```

### VisitDetails
```json
{
  "id": 1,
  "petId": 1,
  "date": "2023-01-15",
  "description": "Regular checkup"
}
```

### VetDetails
```json
{
  "id": 1,
  "firstName": "James",
  "lastName": "Carter",
  "specialties": [
    {
      "id": 1,
      "name": "radiology"
    }
  ]
}
```

### PetInsurance
```json
{
  "petId": 1,
  "insuranceId": 1,
  "premium": 29.99,
  "coverage": "Basic"
}
```

### PaymentDetail
```json
{
  "id": "payment-123",
  "ownerId": 1,
  "petId": 1,
  "amount": 150.00,
  "date": "2023-01-15",
  "description": "Veterinary visit payment"
}
```

### PaymentAdd
```json
{
  "amount": 150.00,
  "description": "Veterinary visit payment"
}
```

### PetNutrition
```json
{
  "petType": "dog",
  "calories": 1200,
  "protein": "25%",
  "fat": "15%",
  "fiber": "4%"
}
```

## Error Handling

The API uses standard HTTP status codes:

- `200 OK` - Successful request
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

Error responses include a message field:
```json
{
  "error": "Resource not found",
  "message": "Owner with ID 999 not found"
}
```

## Circuit Breaker

The API Gateway implements circuit breaker patterns for resilience:
- **Timeout**: 4 seconds default
- **Fallback**: Empty responses for non-critical data (e.g., visits)
- **Protected Endpoints**: `/api/gateway/owners/{ownerId}` includes circuit breaker protection

## Authentication & Authorization

Currently, the API does not implement authentication. In a production environment, consider implementing:
- JWT tokens
- OAuth 2.0
- API keys
- Role-based access control

## Rate Limiting

No rate limiting is currently implemented. Consider adding rate limiting for production use.

## Monitoring & Observability

The application includes:
- **Distributed Tracing**: OpenTelemetry integration
- **Metrics**: Application Signals integration
- **Logging**: Structured logging with trace correlation
- **Health Checks**: Spring Boot Actuator endpoints

## Development Notes

- **Service Discovery**: Uses Netflix Eureka
- **Configuration**: Centralized via Spring Cloud Config
- **Load Balancing**: Client-side load balancing with Ribbon
- **Database**: H2 in-memory database for development
- **Containerization**: Docker support available
- **Cloud Deployment**: AWS ECS, EKS, and EC2 deployment scripts provided

## Mobile App Integration

For mobile app development, focus on these key endpoints:

1. **Owner Management**: CRUD operations for pet owners
2. **Pet Management**: Managing pets and their information
3. **Visit Scheduling**: Booking and viewing veterinary visits
4. **Payment Processing**: Handling payments for services
5. **Insurance Management**: Managing pet insurance policies

The API is RESTful and returns JSON, making it suitable for mobile app consumption.
