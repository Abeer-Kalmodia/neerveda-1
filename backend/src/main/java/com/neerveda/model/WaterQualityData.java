package com.neerveda.model;

public class WaterQualityData {
    private String id;
    private String villageId;
    private String villageName;
    private String district;
    private String state;
    private double latitude;
    private double longitude;
    private double ph;
    private int tds;
    private double turbidity;
    private double temperature;
    private String deviceId;
    private String status;
    private String date;

    // Constructors
    public WaterQualityData() {}

    public WaterQualityData(String id, String villageId, String villageName, String district, String state, 
                            double latitude, double longitude, double ph, int tds, double turbidity, 
                            double temperature, String deviceId, String status, String date) {
        this.id = id;
        this.villageId = villageId;
        this.villageName = villageName;
        this.district = district;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ph = ph;
        this.tds = tds;
        this.turbidity = turbidity;
        this.temperature = temperature;
        this.deviceId = deviceId;
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

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getPh() { return ph; }
    public void setPh(double ph) { this.ph = ph; }

    public int getTds() { return tds; }
    public void setTds(int tds) { this.tds = tds; }

    public double getTurbidity() { return turbidity; }
    public void setTurbidity(double turbidity) { this.turbidity = turbidity; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
