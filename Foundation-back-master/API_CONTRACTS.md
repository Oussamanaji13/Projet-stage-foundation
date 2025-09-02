# Foundation Backend API Contracts

## Overview
This document provides the complete API contracts for the Foundation microservices backend, including request/response payloads, validation rules, and implementation status.

## Services Overview
- **Authentication Service** (auth-service): User registration, login, JWT management
- **User Service** (user-service): User profiles, avatar upload, admin user management
- **Content Service** (content-service): Public content, news, events, partners, contact
- **Social Service** (social-service): Prestations, demandes, avis
- **Notification Service** (notification-service): Email notifications
- **Gateway Service** (gateway-service): API routing and security
- **Discovery Service** (discovery-service): Service discovery
- **Config Service** (config-service): Centralized configuration

---

## 1. Authentication Service

### Register User
**POST** `/api/auth/register`

**Request:**
```json
{
  "firstName": "Jean",
  "lastName": "Dupont",
  "workEmail": "jean.dupont@gov.ma",
  "matricule": "M12345",
  "service": "FINANCE",
  "phone": "+212612345678",
  "password": "S3cur3!Pass"
}
```

**Validation Rules:**
- `firstName`: 2-50 characters, required
- `lastName`: 2-50 characters, required
- `workEmail`: Valid email format, required, must be from allowed domains
- `matricule`: 6-10 alphanumeric characters, required, unique
- `service`: Required
- `phone`: 10 digits, required
- `password`: Min 8 chars, must contain uppercase, lowercase, number, special char

**Response:**
```json
{
  "message": "User registered successfully",
  "userId": "123"
}
```

### Login
**POST** `/api/auth/login`

**Request:**
```json
{
  "workEmail": "jean.dupont@gov.ma",
  "password": "S3cur3!Pass"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresAt": "2025-08-22T12:34:56Z",
  "roles": ["USER"],
  "user": {
    "id": 123,
    "firstName": "Jean",
    "lastName": "Dupont",
    "workEmail": "jean.dupont@gov.ma",
    "service": "FINANCE"
  }
}
```

### Admin: Block/Unblock User
**PUT** `/api/admin/users/{id}/status`

**Request:**
```json
{
  "status": "BLOCKED"
}
```

---

## 2. User Service

### Get My Profile
**GET** `/api/users/me`

**Headers:** `Authorization: Bearer <jwt>`

**Response:**
```json
{
  "email": "jean.dupont@gov.ma",
  "firstName": "Jean",
  "lastName": "Dupont",
  "phone": "+212612345678",
  "matricule": "M12345",
  "serviceCode": "FINANCE",
  "address": "123 Rue de la Paix, Rabat",
  "birthDate": "1995-04-01",
  "familyStatus": "MARRIED",
  "childrenCount": 2,
  "avatarUrl": "/files/avatars/jd.png",
  "notifEmail": true,
  "notifNews": true,
  "notifEvents": false,
  "createdAt": "2025-01-15T10:30:00Z",
  "updatedAt": "2025-01-20T14:45:00Z"
}
```

### Update My Profile
**PUT** `/api/users/me`

**Request:**
```json
{
  "firstName": "Jean",
  "lastName": "Dupont",
  "phone": "+212612345678",
  "address": "123 Rue de la Paix, Rabat",
  "birthDate": "1995-04-01",
  "familyStatus": "MARRIED",
  "childrenCount": 2,
  "notifEmail": true,
  "notifNews": true,
  "notifEvents": false
}
```

### Upload Avatar
**POST** `/api/users/me/avatar`

**Content-Type:** `multipart/form-data`

**Response:**
```json
{
  "avatarUrl": "/files/avatars/jd_123456.png"
}
```

### Admin: List Users
**GET** `/api/admin/users?search=&service=&page=1&size=20`

**Response:**
```json
{
  "content": [
    {
      "id": 123,
      "email": "jean.dupont@gov.ma",
      "firstName": "Jean",
      "lastName": "Dupont",
      "serviceCode": "FINANCE",
      "createdAt": "2025-01-15T10:30:00Z"
    }
  ],
  "totalElements": 150,
  "page": 0,
  "size": 20,
  "totalPages": 8
}
```

### Admin: Update User Roles
**PUT** `/api/admin/users/{id}/roles`

**Request:**
```json
{
  "roles": ["USER", "ADMIN"]
}
```

### Admin: Delete User
**DELETE** `/api/admin/users/{id}`

---

## 3. Content Service

### Public Home Data
**GET** `/api/public/home`

**Response:**
```json
{
  "mission": "Our mission is to support government employees...",
  "stats": {
    "totalMembers": 15000,
    "activePrestations": 25,
    "totalDemandes": 8500
  },
  "ministryContent": "Latest ministry updates..."
}
```

### Public News List
**GET** `/api/public/news?query=&category=&page=1&size=10`

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "title": "New Prestation Available",
      "slug": "new-prestation-available",
      "body": "We are pleased to announce...",
      "imageUrl": "/files/news/news1.jpg",
      "category": "ANNOUNCEMENT",
      "tags": ["prestation", "announcement"],
      "publishedAt": "2025-01-20T10:00:00Z"
    }
  ],
  "totalElements": 45,
  "page": 0,
  "size": 10
}
```

### Public News Detail
**GET** `/api/public/news/{slug}`

**Response:**
```json
{
  "id": 1,
  "title": "New Prestation Available",
  "slug": "new-prestation-available",
  "body": "We are pleased to announce...",
  "imageUrl": "/files/news/news1.jpg",
  "category": "ANNOUNCEMENT",
  "tags": ["prestation", "announcement"],
  "publishedAt": "2025-01-20T10:00:00Z"
}
```

### Public Events
**GET** `/api/public/events?from=today`

**Response:**
```json
[
  {
    "id": 1,
    "title": "Annual Meeting 2025",
    "description": "Join us for our annual meeting...",
    "startDate": "2025-02-15T09:00:00Z",
    "endDate": "2025-02-15T17:00:00Z",
    "location": "Conference Center, Rabat",
    "imageUrl": "/files/events/meeting.jpg"
  }
]
```

### Public Partners
**GET** `/api/public/partners?sector=technology`

**Response:**
```json
[
  {
    "id": 1,
    "name": "TechCorp",
    "logoUrl": "/files/partners/techcorp.png",
    "website": "https://techcorp.ma",
    "sector": "TECHNOLOGY",
    "phone": "+212537123456",
    "email": "contact@techcorp.ma"
  }
]
```

### Contact Form
**POST** `/api/public/contact`

**Request:**
```json
{
  "fullName": "Ahmed Benali",
  "email": "ahmed.benali@example.com",
  "subject": "General Inquiry",
  "message": "I would like to know more about..."
}
```

**Response:** `201 Created`

---

## 4. Social Service

### Prestations List
**GET** `/api/social/prestations?category=&search=&active=true`

**Response:**
```json
[
  {
    "id": 1,
    "title": "Aide au logement",
    "category": "LOGEMENT",
    "maxAmount": 800,
    "durationLabel": "24 mois",
    "conditions": "Âge < 30 ans",
    "description": "Aide financière pour l'acquisition d'un logement...",
    "active": true
  }
]
```

### Create Demande
**POST** `/api/social/demandes`

**Content-Type:** `multipart/form-data`

**Fields:**
- `prestationId`: 1
- `files[]`: [file1.pdf, file2.jpg]
- `address`: "123 Rue de la Paix, Rabat"
- `income`: 5000
- `justification`: "Need housing assistance..."

**Response:**
```json
{
  "id": 12,
  "prestationId": 1,
  "status": "PENDING",
  "submittedAt": "2025-01-20T14:30:00Z"
}
```

### My Demandes
**GET** `/api/social/demandes/my?status=&page=1&size=10`

**Response:**
```json
{
  "content": [
    {
      "id": 12,
      "prestationId": 1,
      "prestationTitle": "Aide au logement",
      "status": "IN_REVIEW",
      "submittedAt": "2025-01-18T13:20:00Z",
      "adminComment": "Documents en cours de vérification"
    }
  ],
  "totalElements": 3,
  "page": 0,
  "size": 10
}
```

### Get Demande Detail
**GET** `/api/social/demandes/{id}`

**Response:**
```json
{
  "id": 12,
  "prestationId": 1,
  "prestationTitle": "Aide au logement",
  "status": "IN_REVIEW",
  "submittedAt": "2025-01-18T13:20:00Z",
  "adminComment": "Documents en cours de vérification",
  "attachments": [
    {
      "id": 1,
      "filename": "document1.pdf",
      "sizeBytes": 1024000
    }
  ]
}
```

### Create Avis
**POST** `/api/social/avis`

**Request:**
```json
{
  "prestationId": 1,
  "rating": 5,
  "comment": "Service excellent, très satisfait"
}
```

**Response:**
```json
{
  "id": 5,
  "prestationId": 1,
  "rating": 5,
  "comment": "Service excellent, très satisfait",
  "status": "PENDING",
  "createdAt": "2025-01-20T15:00:00Z"
}
```

### My Avis
**GET** `/api/social/avis/my`

**Response:**
```json
[
  {
    "id": 5,
    "prestationId": 1,
    "prestationTitle": "Aide au logement",
    "rating": 5,
    "comment": "Service excellent, très satisfait",
    "status": "PUBLISHED",
    "createdAt": "2025-01-20T15:00:00Z"
  }
]
```

---

## 5. Admin Endpoints

### Admin: List Demandes
**GET** `/api/admin/demandes?status=&email=&prestation=&page=1&size=20`

**Response:**
```json
{
  "content": [
    {
      "id": 12,
      "userEmail": "jean.dupont@gov.ma",
      "prestationId": 1,
      "prestationTitle": "Aide au logement",
      "status": "IN_REVIEW",
      "submittedAt": "2025-01-18T13:20:00Z"
    }
  ],
  "totalElements": 150,
  "page": 0,
  "size": 20
}
```

### Admin: Update Demande Status
**PUT** `/api/admin/demandes/{id}/status`

**Request:**
```json
{
  "status": "APPROVED",
  "adminComment": "Dossier complet, approbation accordée"
}
```

### Admin: List Pending Avis
**GET** `/api/admin/avis?status=PENDING&page=1&size=20`

**Response:**
```json
{
  "content": [
    {
      "id": 5,
      "userEmail": "jean.dupont@gov.ma",
      "prestationId": 1,
      "prestationTitle": "Aide au logement",
      "rating": 5,
      "comment": "Service excellent, très satisfait",
      "status": "PENDING",
      "createdAt": "2025-01-20T15:00:00Z"
    }
  ],
  "totalElements": 25,
  "page": 0,
  "size": 20
}
```

### Admin: Approve Avis
**PUT** `/api/admin/avis/{id}/approve`

### Admin: Reject Avis
**PUT** `/api/admin/avis/{id}/reject`

---

## 6. Notification Service

### Send Email
**POST** `/api/notify/email`

**Request:**
```json
{
  "to": "user@example.com",
  "subject": "Your demande status has been updated",
  "html": "<h1>Status Update</h1><p>Your demande has been approved.</p>",
  "from": "noreply@foundation.com",
  "replyTo": "support@foundation.com"
}
```

**Response:**
```json
{
  "message": "Email sent successfully",
  "notificationId": "123",
  "success": true
}
```

---

## 7. Error Responses

### Validation Error
```json
{
  "timestamp": "2025-01-20T15:30:00Z",
  "status": 400,
  "error": "ValidationFailed",
  "messages": [
    "Email invalide",
    "Mot de passe trop court"
  ]
}
```

### Not Found Error
```json
{
  "timestamp": "2025-01-20T15:30:00Z",
  "status": 404,
  "error": "ResourceNotFound",
  "message": "User not found"
}
```

### Unauthorized Error
```json
{
  "timestamp": "2025-01-20T15:30:00Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or expired token"
}
```

---

## 8. Implementation Status

### ✅ Completed
- Authentication Service: Entities, DTOs, basic structure
- User Service: Entities, DTOs, repositories, service interfaces
- Content Service: Entities, DTOs, repositories, public controller
- Social Service: Entities, DTOs, repositories, service interfaces, public controller
- Notification Service: Complete implementation with email logging

### 🔄 In Progress
- Authentication Service: Service implementation, controllers, JWT
- User Service: Service implementation, controllers, avatar upload
- Content Service: Admin controllers, service implementation
- Social Service: Admin controllers, service implementation, file upload

### 📋 Pending
- Gateway service configuration
- Security configuration
- Error handling
- File upload configuration
- Database migrations
- Integration testing

---

## 9. Development Setup

### Prerequisites
- Java 17
- Maven
- Docker & Docker Compose
- MySQL 8.0

### Running the Services
```bash
# Start all services
docker-compose up -d

# Or run individually
cd auth-service && ./mvnw spring-boot:run
cd user-service && ./mvnw spring-boot:run
cd content-service && ./mvnw spring-boot:run
cd social-service && ./mvnw spring-boot:run
cd notification-service && ./mvnw spring-boot:run
```

### Swagger Documentation
- Gateway: http://localhost:8080/swagger-ui.html
- Auth Service: http://localhost:8081/swagger-ui.html
- User Service: http://localhost:8082/swagger-ui.html
- Content Service: http://localhost:8083/swagger-ui.html
- Social Service: http://localhost:8084/swagger-ui.html
- Notification Service: http://localhost:8085/swagger-ui.html

---

## 10. Testing Scenarios

### User Journey
1. Register new user
2. Login and get JWT token
3. Access profile page
4. Update profile information
5. Upload avatar
6. Browse prestations
7. Create demande with files
8. Submit avis
9. Check demande status updates

### Admin Journey
1. Login as admin
2. Manage users (list, search, update roles)
3. Manage content (news, events, partners)
4. Process demandes (update status)
5. Moderate avis (approve/reject)
6. View contact messages

### Public Journey
1. View home page
2. Browse news articles
3. View upcoming events
4. Submit contact form
5. View partners directory
