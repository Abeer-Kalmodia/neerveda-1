package com.neerveda.model;

public class Alert {
    private String id;
    private String villageId;
    private String villageName;
    private String alertParameter;
    private String alertMessage;
    private String severity;
    private String date;

    // Constructors
    public Alert() {}

    public Alert(String id, String villageId, String villageName, String alertParameter, 
                 String alertMessage, String severity, String date) {
        this.id = id;
        this.villageId = villageId;
        this.villageName = villageName;
        this.alertParameter = alertParameter;
        this.alertMessage = alertMessage;
        this.severity = severity;
        this.date = date;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getVillageId() { return villageId; }
    public void setVillageId(String villageId) { this.villageId = villageId; }

    public String getVillageName() { return villageName; }
    public void setVillageName(String villageName) { this.villageName = villageName; }

    public String getAlertParameter() { return alertParameter; }
    public void setAlertParameter(String alertParameter) { this.alertParameter = alertParameter; }

    public String getAlertMessage() { return alertMessage; }
    public void setAlertMessage(String alertMessage) { this.alertMessage = alertMessage; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
