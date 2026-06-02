package com.neerveda.model;

public class PredictionResponse {

    private String outbreakRisk;
    private double confidence;

    public String getOutbreakRisk() {
        return outbreakRisk;
    }

    public void setOutbreakRisk(String outbreakRisk) {
        this.outbreakRisk = outbreakRisk;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}