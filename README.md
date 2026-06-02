
# NeerVeda

NeerVeda is a community-focused water safety and early warning platform that helps detect and respond to waterborne disease risks. This repository contains a Spring Boot backend and a simple frontend PWA for demo and local testing.

## Highlights
- Interactive water-quality map and telemetry viewer
- Historical charts for key water parameters (pH, TDS, turbidity)
- Symptom reporting workflow for community health workers
- Offline-capable frontend (PWA) with service-worker sync
- Backend alerts and simulated notification logs

## Repository layout

```
neerveda/
├── README.md
├── frontend/                 # Static PWA (index.html, manifest.json, sw.js)
└── backend/                  # Spring Boot service (Maven)
    ├── pom.xml
    └── src/main/java/com/neerveda
        ├── config/          # Configuration (Firebase, app settings)
        ├── controller/      # REST controllers
        ├── model/           # Domain models
        └── service/         # Business logic
```

## Tech stack

- Backend: Java 21, Spring Boot 3.2.x (Maven)
- Frontend: Plain HTML/CSS/JavaScript, Leaflet, Chart.js, PWA service worker
- Optional: Firebase Admin SDK for push/auth integrations (configured in backend)

## Prerequisites

- Java 21 (or compatible JDK)
- Maven (for building/running the backend)
- Python 3 (optional: simple static server for the frontend)

## Run locally

1. Start the backend (from the repo root):

```bash
cd backend
mvn spring-boot:run
```

The backend listens on http://localhost:8080 by default.

2. Serve the frontend (any static server). From the repo root:

```bash
cd frontend
python -m http.server 3000
```

Open http://localhost:3000 in your browser.

Notes:
- Application settings live in `backend/src/main/resources/application.properties`.
- Firebase credentials are optional; see `backend/src/main/java/com/neerveda/config/FirebaseConfig.java` for details.

## API & demo accounts

- API endpoints include symptom reporting and water-quality telemetry (see `backend/src/main/java/com/neerveda/controller`).
- Demo accounts are seeded for local testing (credentials may be included in test data).

## Contributing

Suggestions, fixes, and documentation improvements are welcome. Open an issue or submit a pull request.

## License

This project is provided for demonstration and research. Add a license file if you intend to publish or redistribute.
```