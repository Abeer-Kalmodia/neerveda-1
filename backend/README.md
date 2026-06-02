#  NeerVeda Backend

Backend service for the **NeerVeda Smart Water Monitoring & Management System**.

The backend provides secure REST APIs for authentication, water quality management, report handling, user management, and integration with Firebase services.

---

## 📖 Overview

NeerVeda is a water resource monitoring platform designed to:

- Monitor water quality parameters
- Manage inspections and reports
- Store and retrieve environmental data
- Provide secure user authentication
- Support real-time and cloud-based data management

---

##  Features

### Authentication & Security
- JWT Authentication
- Role-Based Authorization
- Secure Password Encryption
- Protected REST Endpoints

### User Management
- User Registration
- User Login
- Profile Management
- Role Management

### Water Quality Management
- Create Water Records
- Update Water Parameters
- View Historical Data
- Manage Inspection Reports

### Cloud Integration
- Firebase Firestore
- Firebase Admin SDK
- Cloud Data Storage

---

##  Technology Stack

| Technology | Purpose |
|------------|----------|
| Java 21 | Programming Language |
| Spring Boot 3 | Backend Framework |
| Spring Security | Authentication & Authorization |
| Spring Data JPA | Data Access Layer |
| Maven | Dependency Management |
| Firebase Firestore | Cloud Database |
| JWT | Token-Based Authentication |
| Render | Backend Deployment |

---

## 📂 Project Structure

```text
backend
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.neerveda
│   │   │       ├── config
│   │   │       ├── controller
│   │   │       ├── dto
│   │   │       ├── entity
│   │   │       ├── repository
│   │   │       ├── security
│   │   │       ├── service
│   │   │       └── NeervedaApplication.java
│   │   │
│   │   └── resources
│   │       ├── application.properties
│   │       └── firebase-service-account.json
│   │
│   └── test
│
├── pom.xml
└── README.md
```

---

## ⚙ Prerequisites

Install:

- Java 21+
- Maven 3.9+
- Git
- Firebase Account

Verify installation:

```bash
java -version
mvn -version
```

---

## 🔥 Firebase Configuration

### Step 1: Create Firebase Project

Create a Firebase project named:

```text
neerveda
```

### Step 2: Enable Firestore

```text
Firebase Console
→ Build
→ Firestore Database
→ Create Database
```

### Step 3: Generate Service Account Key

```text
Project Settings
→ Service Accounts
→ Generate New Private Key
```

### Step 4: Add Credentials

Place the downloaded JSON file in:

```text
src/main/resources/firebase-service-account.json
```

---

##  Environment Variables

### Windows

```cmd
set JWT_SECRET=your_secret_key
```

### Linux/Mac

```bash
export JWT_SECRET=your_secret_key
```

---

## ▶ Running Locally

### Clone Repository

```bash
git clone https://github.com/Abeer-Kalmodia/neerveda-1.git
```

### Navigate to Backend

```bash
cd backend
```

### Install Dependencies

```bash
mvn clean install
```

### Start Server

```bash
mvn spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

---

##  Authentication

### Login

```http
POST /api/auth/login
```

### Register

```http
POST /api/auth/register
```

### JWT Header

```http
Authorization: Bearer <token>
```

---

## 📡 Sample API Endpoints

### Authentication

| Method | Endpoint |
|----------|----------|
| POST | /api/auth/register |
| POST | /api/auth/login |

### Water Quality

| Method | Endpoint |
|----------|----------|
| GET | /api/water |
| POST | /api/water |
| PUT | /api/water/{id} |
| DELETE | /api/water/{id} |

### Reports

| Method | Endpoint |
|----------|----------|
| GET | /api/reports |
| POST | /api/reports |

---

##  Testing

Run unit tests:

```bash
mvn test
```

---

## ☁ Deployment

### Render Deployment

Build Command:

```bash
mvn clean package
```

Start Command:

```bash
java -jar target/*.jar
```

Required Environment Variables:

```text
JWT_SECRET
FIREBASE_SERVICE_ACCOUNT
```

---

##  Future Enhancements

- IoT Sensor Integration
- Real-Time Monitoring
- GIS-Based Water Mapping
- AI Water Quality Prediction
- SMS & Email Alerts
- Mobile Application Support

---

##  Contributors

- Aditya Tiwari
- Abeer Kalmodia

---

## 📄 License

This project is developed for educational and research purposes.

---

### 🌊 NeerVeda – Smart Water Monitoring & Management System
