package com.neerveda.service;

import com.neerveda.service.AIService;
import com.google.cloud.firestore.*;
import com.neerveda.config.FirebaseConfig;
import com.neerveda.model.Alert;
import com.neerveda.model.PredictionResponse;
import com.neerveda.model.SymptomReport;
import com.neerveda.model.WaterQualityData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WaterQualityService {
    private static final Logger logger = LoggerFactory.getLogger(WaterQualityService.class);

    @Autowired
    private AIService aiService;

    @Autowired(required = false)
    private Firestore firestore;

    @Autowired
    private FirebaseConfig firebaseConfig;

    // Thread-safe in-memory databases used as database fallbacks
    private final Map<String, WaterQualityData> inMemoryReadings = new ConcurrentHashMap<>();
    private final Map<String, SymptomReport> inMemorySymptoms = new ConcurrentHashMap<>();

    @PostConstruct
    public void seedMockDatabase() {
        logger.info("Initializing in-memory seed database for Rural Northeast India...");
        
        // Seed water readings
        WaterQualityData r1 = new WaterQualityData("READ-001", "VIL001", "Dimapur Village", "Dimapur", "Nagaland", 
                                                25.9022, 93.7234, 5.8, 620, 8.5, 28.0, "ESP32-001", "DANGER", Instant.now().minusSeconds(86400).toString());
        WaterQualityData r2 = new WaterQualityData("READ-002", "VIL002", "Kohima Reservoir", "Kohima", "Nagaland", 
                                                25.6751, 94.1086, 7.2, 180, 1.8, 21.5, "ESP32-002", "SAFE", Instant.now().minusSeconds(43200).toString());
        WaterQualityData r3 = new WaterQualityData("READ-003", "VIL003", "Chumukedima Pump", "Dimapur", "Nagaland", 
                                                25.8242, 93.7712, 6.9, 480, 3.1, 27.0, "ESP32-003", "SAFE", Instant.now().minusSeconds(14400).toString());
        WaterQualityData r4 = new WaterQualityData("READ-004", "VIL004", "Mokokchung Spring", "Mokokchung", "Nagaland", 
                                                26.3262, 94.5132, 7.8, 95, 0.9, 19.0, "ESP32-004", "SAFE", Instant.now().toString());

        inMemoryReadings.put(r1.getId(), r1);
        inMemoryReadings.put(r2.getId(), r2);
        inMemoryReadings.put(r3.getId(), r3);
        inMemoryReadings.put(r4.getId(), r4);

        // Seed symptom reports
        SymptomReport s1 = new SymptomReport("SYMP-001", "VIL001", "Dimapur Village", "Dimapur", "Diarrhea", 12, 
                                            "ASHA Worker Preeti Das", "Sudden spike in diarrhea cases after heavy rainfall. Residents suspect Ground Well contamination.", "ACTIVE", Instant.now().minusSeconds(64800).toString());
        SymptomReport s2 = new SymptomReport("SYMP-002", "VIL003", "Chumukedima Pump", "Dimapur", "Typhoid", 4, 
                                            "Volunteer John", "High fever cases matching typhoid reported in youngsters drinking from public pump.", "REVIEWED", Instant.now().minusSeconds(129600).toString());

        inMemorySymptoms.put(s1.getId(), s1);
        inMemorySymptoms.put(s2.getId(), s2);
        
        logger.info("Seed data loaded successfully ({} water records, {} symptom reports).", inMemoryReadings.size(), inMemorySymptoms.size());
    }

    // Evaluate Water safety
    public WaterQualityData processReading(WaterQualityData data) {
        if (data.getId() == null || data.getId().isEmpty()) {
            data.setId("READ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (data.getDate() == null || data.getDate().isEmpty()) {
            data.setDate(Instant.now().toString());
        }

        // Apply WHO threshold checks
        List<String> failedParams = new ArrayList<>();
        String status = "SAFE";
        
        if (data.getPh() < 6.5 || data.getPh() > 8.5) {
            failedParams.add("pH (" + data.getPh() + ")");
            status = "DANGER";
        }
        if (data.getTds() >= 500) {
            failedParams.add("TDS (" + data.getTds() + " ppm)");
            status = "DANGER";
        }
        if (data.getTurbidity() >= 5.0) {
            failedParams.add("Turbidity (" + data.getTurbidity() + " NTU)");
            status = "DANGER";
        }
        if (data.getTemperature() >= 35.0) {
            failedParams.add("Temperature (" + data.getTemperature() + "°C)");
            status = "DANGER";
        }

        // Borderline moderate safety checks
        if (status.equals("SAFE")) {
            if ((data.getPh() >= 6.3 && data.getPh() < 6.5) || 
                (data.getPh() > 8.5 && data.getPh() <= 8.8) || 
                (data.getTds() >= 400 && data.getTds() < 500) || 
                (data.getTurbidity() >= 4.0 && data.getTurbidity() < 5.0)) {
                status = "MODERATE";
            }
        }

        data.setStatus(status);

        PredictionResponse prediction =
        aiService.predict(
                data.getPh(),
                data.getTds(),
                data.getTurbidity(),
                data.getTemperature()
        );

        data.setAiRisk(
                prediction.getOutbreakRisk());

        data.setAiConfidence(
                prediction.getConfidence());

        // Save
        saveReading(data);

        // Alert check and Twilio Simulation
        if (status.equals("DANGER")) {
            String alertParams = String.join(", ", failedParams);
            String message = "⚠️ Emergency Alert: Danger water index recorded at " + data.getVillageName() + 
                             " (ID: " + data.getVillageId() + ") - Out of bounds parameters: " + alertParams;
            
            simulateTwilioSms(data.getVillageName(), alertParams, message);
        }

        return data;
    }

    private void saveReading(WaterQualityData data) {
        if (firebaseConfig.isFirebaseEnabled() && firestore != null) {
            try {
                firestore.collection("readings").document(data.getId()).set(data);
                logger.info("Saved reading [{}] to Firestore Database.", data.getId());
            } catch (Exception e) {
                logger.error("Error saving to Firestore, caching to memory: {}", e.getMessage());
                inMemoryReadings.put(data.getId(), data);
            }
        } else {
            inMemoryReadings.put(data.getId(), data);
            logger.info("Saved reading [{}] to In-Memory Collection.", data.getId());
        }
    }

    public List<WaterQualityData> getAllReadings() {
        if (firebaseConfig.isFirebaseEnabled() && firestore != null) {
            try {
                List<QueryDocumentSnapshot> documents = firestore.collection("readings").get().get().getDocuments();
                List<WaterQualityData> list = new ArrayList<>();
                for (QueryDocumentSnapshot doc : documents) {
                    list.add(doc.toObject(WaterQualityData.class));
                }
                // Sort by date desc
                list.sort((a, b) -> b.getDate().compareTo(a.getDate()));
                return list;
            } catch (Exception e) {
                logger.error("Failed to query Firestore readings: {}. Returning in-memory list.", e.getMessage());
            }
        }
        
        List<WaterQualityData> list = new ArrayList<>(inMemoryReadings.values());
        list.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        return list;
    }

    public WaterQualityData getReadingById(String id) {
        if (firebaseConfig.isFirebaseEnabled() && firestore != null) {
            try {
                DocumentSnapshot doc = firestore.collection("readings").document(id).get().get();
                if (doc.exists()) {
                    return doc.toObject(WaterQualityData.class);
                }
            } catch (Exception e) {
                logger.error("Failed to query Firestore reading by id: {}", e.getMessage());
            }
        }
        return inMemoryReadings.get(id);
    }

    public String getVillageStatus(String villageId) {
        List<WaterQualityData> villageReads = getAllReadings().stream()
                .filter(r -> r.getVillageId().equalsIgnoreCase(villageId))
                .collect(Collectors.toList());

        if (villageReads.isEmpty()) {
            return "UNKNOWN";
        }

        // Return status of latest reading
        return villageReads.get(0).getStatus();
    }

    public List<Alert> getDangerousReadings() {
        List<WaterQualityData> dangerReads = getAllReadings().stream()
                .filter(r -> "DANGER".equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toList());

        List<Alert> alerts = new ArrayList<>();
        for (WaterQualityData r : dangerReads) {
            List<String> failed = new ArrayList<>();
            if (r.getPh() < 6.5 || r.getPh() > 8.5) failed.add("pH (" + r.getPh() + ")");
            if (r.getTds() >= 500) failed.add("TDS (" + r.getTds() + ")");
            if (r.getTurbidity() >= 5.0) failed.add("Turbidity (" + r.getTurbidity() + ")");
            if (r.getTemperature() >= 35.0) failed.add("Temp (" + r.getTemperature() + ")");

            String params = String.join(", ", failed);
            String message = "⚠️ Water safety index Danger in " + r.getVillageName() + ". Out of range: " + params;
            alerts.add(new Alert("ALT-" + r.getId().replace("READ-", ""), r.getVillageId(), r.getVillageName(), params, message, "DANGER", r.getDate()));
        }
        return alerts;
    }

    // Outbreak symptoms handling
    public SymptomReport saveSymptomReport(SymptomReport report) {
        if (report.getId() == null || report.getId().isEmpty()) {
            report.setId("SYMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (report.getDate() == null || report.getDate().isEmpty()) {
            report.setDate(Instant.now().toString());
        }
        if (report.getStatus() == null || report.getStatus().isEmpty()) {
            report.setStatus("ACTIVE");
        }

        if (firebaseConfig.isFirebaseEnabled() && firestore != null) {
            try {
                firestore.collection("symptoms").document(report.getId()).set(report);
                logger.info("Saved symptom report [{}] to Firestore.", report.getId());
                return report;
            } catch (Exception e) {
                logger.error("Firestore save failed for symptom report: {}. Using memory.", e.getMessage());
            }
        }

        inMemorySymptoms.put(report.getId(), report);
        logger.info("Saved symptom report [{}] to In-Memory DB.", report.getId());
        return report;
    }

    public List<SymptomReport> getAllSymptoms() {
        if (firebaseConfig.isFirebaseEnabled() && firestore != null) {
            try {
                List<QueryDocumentSnapshot> documents = firestore.collection("symptoms").get().get().getDocuments();
                List<SymptomReport> list = new ArrayList<>();
                for (QueryDocumentSnapshot doc : documents) {
                    list.add(doc.toObject(SymptomReport.class));
                }
                list.sort((a, b) -> b.getDate().compareTo(a.getDate()));
                return list;
            } catch (Exception e) {
                logger.error("Failed to query Firestore symptoms: {}", e.getMessage());
            }
        }

        List<SymptomReport> list = new ArrayList<>(inMemorySymptoms.values());
        list.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        return list;
    }

    public List<SymptomReport> getSymptomsByVillage(String villageId) {
        return getAllSymptoms().stream()
                .filter(s -> s.getVillageId().equalsIgnoreCase(villageId))
                .collect(Collectors.toList());
    }

    public SymptomReport reviewSymptomReport(String id) {
        SymptomReport report = null;

        if (firebaseConfig.isFirebaseEnabled() && firestore != null) {
            try {
                DocumentReference docRef = firestore.collection("symptoms").document(id);
                DocumentSnapshot doc = docRef.get().get();
                if (doc.exists()) {
                    report = doc.toObject(SymptomReport.class);
                    if (report != null) {
                        String newStatus = report.getStatus().equalsIgnoreCase("ACTIVE") ? "RESOLVED" : "ACTIVE";
                        report.setStatus(newStatus);
                        docRef.set(report);
                        logger.info("Symptom report [{}] status toggled to {} in Firestore.", id, newStatus);
                        return report;
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to update Firestore symptom review: {}", e.getMessage());
            }
        }

        report = inMemorySymptoms.get(id);
        if (report != null) {
            String newStatus = report.getStatus().equalsIgnoreCase("ACTIVE") ? "RESOLVED" : "ACTIVE";
            report.setStatus(newStatus);
            inMemorySymptoms.put(id, report);
            logger.info("Symptom report [{}] status toggled to {} in Memory DB.", id, newStatus);
        }
        return report;
    }

    // Twilio SMS Simulation Logger
    private void simulateTwilioSms(String village, String params, String alertMessage) {
        logger.info("\n============================================================" +
                    "\n📲 [SIMULATION - TWILIO SMS SYSTEM OUTPUT]" +
                    "\n📤 From: +14155550199 (NeerVeda Twilio SMS Service)" +
                    "\n📥 To: +919402123456 (District Medical Officer, " + village + ")" +
                    "\n💬 Alert Body: " + alertMessage +
                    "\nℹ️ Automated Action: Dispatched disease advisory bulletin." +
                    "\n============================================================\n");
    }
}
