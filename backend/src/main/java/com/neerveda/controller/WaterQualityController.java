package com.neerveda.controller;

import com.neerveda.dto.ApiResponse;
import com.neerveda.model.Alert;
import com.neerveda.model.WaterQualityData;
import com.neerveda.service.WaterQualityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/water")
@CrossOrigin(origins = "*") // Allow any client origin (frontend) to query
public class WaterQualityController {
    private static final Logger logger = LoggerFactory.getLogger(WaterQualityController.class);

    @Autowired
    private WaterQualityService waterService;

    @GetMapping("/health")
    public ResponseEntity<ApiResponse> checkHealth(@RequestHeader(value = "X-CSRF-Token", required = false) String csrfToken) {
        logger.info("GET /api/v1/water/health received. CSRF Header: {}", csrfToken);
        return ResponseEntity.ok(new ApiResponse(true, "Backend API online", null));
    }

    @PostMapping("/reading")
    public ResponseEntity<ApiResponse> recordReading(
            @RequestHeader(value = "X-CSRF-Token", required = false) String csrfToken,
            @RequestBody WaterQualityData reading) {
        logger.info("POST /api/v1/water/reading received. CSRF Header: {}", csrfToken);
        
        // Custom CSRF verification simulation (logs validation warning if missing)
        if (csrfToken == null || csrfToken.isEmpty()) {
            logger.warn("CSRF header X-CSRF-Token is missing! Request processed with warnings.");
        }

        try {
            WaterQualityData processed = waterService.processReading(reading);
            String message = "Water quality reading recorded. Status: " + processed.getStatus();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, message, processed));
        } catch (Exception e) {
            logger.error("Failed to record reading: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to record reading: " + e.getMessage(), null));
        }
    }

    @GetMapping("/readings")
    public ResponseEntity<ApiResponse> getAllReadings() {
        logger.info("GET /api/v1/water/readings received.");
        List<WaterQualityData> list = waterService.getAllReadings();
        return ResponseEntity.ok(new ApiResponse(true, "Successfully retrieved all water readings", list));
    }

    @GetMapping("/reading/{id}")
    public ResponseEntity<ApiResponse> getReadingById(@PathVariable String id) {
        logger.info("GET /api/v1/water/reading/{} received.", id);
        WaterQualityData data = waterService.getReadingById(id);
        if (data != null) {
            return ResponseEntity.ok(new ApiResponse(true, "Found reading ID " + id, data));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Reading not found with ID " + id, null));
        }
    }

    @GetMapping("/status/{villageId}")
    public ResponseEntity<ApiResponse> getVillageStatus(@PathVariable String villageId) {
        logger.info("GET /api/v1/water/status/{} received.", villageId);
        String status = waterService.getVillageStatus(villageId);
        return ResponseEntity.ok(new ApiResponse(true, "Retrieved status for village " + villageId, status));
    }

    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse> getDangerousReadings() {
        logger.info("GET /api/v1/water/alerts received.");
        List<Alert> alerts = waterService.getDangerousReadings();
        return ResponseEntity.ok(new ApiResponse(true, "Retrieved all active danger alerts", alerts));
    }
}
