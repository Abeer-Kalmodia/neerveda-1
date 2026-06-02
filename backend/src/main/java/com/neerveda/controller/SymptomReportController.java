package com.neerveda.controller;

import com.neerveda.dto.ApiResponse;
import com.neerveda.model.SymptomReport;
import com.neerveda.service.WaterQualityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/symptoms")
@CrossOrigin(origins = "*")
public class SymptomReportController {
    private static final Logger logger = LoggerFactory.getLogger(SymptomReportController.class);

    @Autowired
    private WaterQualityService waterService;

    @PostMapping("/report")
    public ResponseEntity<ApiResponse> createSymptomReport(
            @RequestHeader(value = "X-CSRF-Token", required = false) String csrfToken,
            @RequestBody SymptomReport report) {
        logger.info("POST /api/v1/symptoms/report received. CSRF Header: {}", csrfToken);
        
        if (csrfToken == null || csrfToken.isEmpty()) {
            logger.warn("CSRF header X-CSRF-Token missing on symptom report dispatch.");
        }

        try {
            SymptomReport saved = waterService.saveSymptomReport(report);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Symptom report submitted successfully", saved));
        } catch (Exception e) {
            logger.error("Failed to submit report: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to submit report: " + e.getMessage(), null));
        }
    }

    @GetMapping("/reports")
    public ResponseEntity<ApiResponse> getAllReports() {
        logger.info("GET /api/v1/symptoms/reports received.");
        List<SymptomReport> reports = waterService.getAllSymptoms();
        return ResponseEntity.ok(new ApiResponse(true, "Successfully retrieved all symptom reports", reports));
    }

    @GetMapping("/reports/{villageId}")
    public ResponseEntity<ApiResponse> getReportsByVillage(@PathVariable String villageId) {
        logger.info("GET /api/v1/symptoms/reports/{} received.", villageId);
        List<SymptomReport> reports = waterService.getSymptomsByVillage(villageId);
        return ResponseEntity.ok(new ApiResponse(true, "Retrieved reports for village " + villageId, reports));
    }

    @PutMapping("/report/{id}/review")
    public ResponseEntity<ApiResponse> reviewReport(
            @RequestHeader(value = "X-CSRF-Token", required = false) String csrfToken,
            @PathVariable String id) {
        logger.info("PUT /api/v1/symptoms/report/{}/review received. CSRF Header: {}", id, csrfToken);

        if (csrfToken == null || csrfToken.isEmpty()) {
            logger.warn("CSRF header X-CSRF-Token missing on symptom review request.");
        }

        try {
            SymptomReport reviewed = waterService.reviewSymptomReport(id);
            if (reviewed != null) {
                return ResponseEntity.ok(new ApiResponse(true, "Report " + id + " updated to " + reviewed.getStatus(), reviewed));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(false, "Symptom report not found with ID " + id, null));
            }
        } catch (Exception e) {
            logger.error("Failed to review report {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to review report: " + e.getMessage(), null));
        }
    }
}
