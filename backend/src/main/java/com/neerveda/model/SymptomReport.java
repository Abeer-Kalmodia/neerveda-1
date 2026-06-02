package com.neerveda.model;

public class SymptomReport {
    private String id;
    private String villageId;
    private String villageName;
    private String district;
    private String symptomType;
    private int casesCount;
    private String reportedBy;
    private String description;
    private String status;
    private String date;

    // Constructors
    public SymptomReport() {}

    public SymptomReport(String id, String villageId, String villageName, String district, String symptomType, 
                         int casesCount, String reportedBy, String description, String status, String date) {
        this.id = id;
        this.villageId = villageId;
        this.villageName = villageName;
        this.district = district;
        this.symptomType = symptomType;
        this.casesCount = casesCount;
        this.reportedBy = reportedBy;
        this.description = description;
        this.status = status;
        this.date = date;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getVillageId() { return villageId; }
    public void setVillageId(String villageId) { this.villageId = villageId; }

    public String getVillageName() { return villageName; }
    public void setVillageName(String villageName) { this.villageName = villageName; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getSymptomType() { return symptomType; }
    public void setSymptomType(String symptomType) { this.symptomType = symptomType; }

    public int getCasesCount() { return casesCount; }
    public void setCasesCount(int casesCount) { this.casesCount = casesCount; }

    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
