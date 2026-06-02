# NeerVeda — Water Wisdom Portal & Epidemic Early Warning System

NeerVeda is a premium, full-stack, community-driven water safety surveillance platform designed to prevent disease outbreaks in rural India (specifically Nagaland and Northeast India). 

It features an IoT-simulated telemetry dashboard, real-time spatial mapping, automated outbreak detection thresholds, local-first offline syncing, and SMS notifications simulation to bridge the gap between ASHA workers, community volunteers, and health officers.

---

## 🚀 Key Features

* **Interactive Water Quality Spatial Map**: Integrates Leaflet.js with light-themed CartoDB Voyager tiles, rendering real-time safety status pins (`SAFE` / `MODERATE` / `DANGER`) based on chemical water readings (pH, TDS, Turbidity, Temp). Click maps directly to auto-fill telemetry coordinates.
* **Historical Parameter Analytics**: Dynamically plots telemetry history curves using Chart.js to track pH levels, Turbidity (NTU), and Total Dissolved Solids (TDS/ppm).
* **ASHA Outbreak Symptom Reporting**: Interactive reporting portal for waterborne illnesses (Diarrhea, Cholera, Typhoid, Dysentery). Auto-dispatches warnings to local health officers.
* **Firewall Security Logs Console**: Client-side monitoring panel tracing generated CSRF tokens, session state signatures, input validation, and login lockout status.
* **PWA Offline Capabilities**: Configured Manifest and Service Worker caching. Pending telemetry submissions are stored in an offline queue during connectivity drops and auto-sync immediately when backend connection resumes.
* **Automated Safety Dispatcher**: Backend checks telemetry against biological thresholds and logs alert events, simulating real-time SMS dispatches for dangerous water flags.

---

## 📁 Repository Structure

```
neerveda/
├── README.md
├── .gitignore
├── frontend/
│   ├── index.html        # Unified Single Page Application (Auth + Dashboard)
│   ├── logo.svg          # Brand vector assets
│   ├── manifest.json     # PWA manifest configurations
│   └── sw.js             # Caching & offline sync service worker
└── backend/
    ├── pom.xml           # Spring Boot configuration (Java 21, Spring 3.2.5)
    └── src/              # Application controller, service, config, and models
```

---

## 🛠️ Technology Stack

* **Frontend**: Vanilla HTML5, CSS3 Custom Properties (featuring Navy/Teal glassmorphism UI & custom autofill overrides), Vanilla Javascript (ES6), Lucide Icons, Leaflet.js Maps, Chart.js.
* **Backend**: Java 21, Spring Boot 3.2.5, Spring Web, Firebase Admin SDK (with an automated mock registry fallback if credentials are omitted), Twilio SMS simulated logs.

---

## ⚙️ Running Locally

### 1. Prerequisite: Java & Python
Make sure you have **Java 21** and **Python 3** installed on your system.

### 2. Startup the Backend Service
Navigate to the `backend` folder and run it using Maven:
```bash
cd backend
mvn spring-boot:run
```
* The server will boot on **[http://localhost:8080](http://localhost:8080)**.
* Database collections are automatically seeded on startup with sample village readings and active outbreak records.

### 3. Startup the Frontend Server
Navigate to the `frontend` folder and serve the static files:
```bash
cd frontend
python -m http.server 3000
```
Open **[http://localhost:3000](http://localhost:3000)** in your browser.

---

## 🔑 Demo Access Profiles

You can log in to the dashboard using these pre-seeded simulation profiles:

* **ASHA Community Worker**:
  * **Email**: `asha@neerveda.org`
  * **Password**: `Asha@1234`
* **District Health Officer**:
  * **Email**: `officer@neerveda.org`
  * **Password**: `Officer@1234`
