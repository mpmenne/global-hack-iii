package com.globalhack;

/**
 * Created by Tim on 12/6/2014.
 */

public class SensiState {

    private Double currentTemperature;
    private Double heatSetPoint;
    private Double coolSetPoint;
    private String operatingMode;
    private Boolean away;
    private String operatingSince;

    public String getOperatingSince() {
        return operatingSince;
    }

    public void setOperatingSince(String operatingSince) {
        this.operatingSince = operatingSince;
    }

    public Boolean getAway() {
        return away;
    }

    public void setAway(Boolean away) {
        this.away = away;
    }

    public Double getCurrentTemperature() {
        return currentTemperature;
    }

    public void setCurrentTemperature(Double currentTemperature) {
        if(this.currentTemperature != currentTemperature) {
            System.out.println("Temperature updated to "+ currentTemperature.toString());
        }
        this.currentTemperature = currentTemperature;
    }

    public Double getHeatSetPoint() {
        return heatSetPoint;
    }

    public void setHeatSetPoint(Double heatSetPoint) {

        try {
            if (!this.heatSetPoint.equals(heatSetPoint)) {
                this.heatSetPoint = heatSetPoint;
                this.away = false;
            }
        } catch (NullPointerException e) {
            this.heatSetPoint = heatSetPoint;
            System.out.println("Setting for the first time.");
        }
    }

    public Double getCoolSetPoint() {
        return coolSetPoint;
    }

    public void setCoolSetPoint(Double coolSetPoint) {
        try {
            if (!this.coolSetPoint.equals(coolSetPoint)) {
                this.coolSetPoint = coolSetPoint;
                this.away = false;
            }
        } catch (NullPointerException e) {
            this.coolSetPoint = coolSetPoint;
            System.out.println("Setting for the first time.");
        }
    }

    public String getOperatingMode() {
        return operatingMode;
    }

    public void setOperatingMode(String operatingMode) {
        this.operatingMode = operatingMode;
    }
}
