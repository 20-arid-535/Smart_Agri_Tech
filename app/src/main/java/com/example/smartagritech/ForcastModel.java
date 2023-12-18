package com.example.smartagritech;

public class ForcastModel {
    private String date;
    private String mintemperature;
    private String maxtemperature;
    private String icon;

    private String humidity;

    public ForcastModel(String date, String mintemperature, String maxtemperature, String icon, String humidity) {
        this.date = date;
        this.mintemperature = mintemperature;
        this.maxtemperature = maxtemperature;
        this.icon = icon;

        this.humidity = humidity;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getMintemperature() {
        return mintemperature;
    }

    public void setMintemperature(String mintemperature) {
        this.mintemperature = mintemperature;
    }

    public String getMaxtemperature() {
        return maxtemperature;
    }

    public void setMaxtemperature(String maxtemperature) {
        this.maxtemperature = maxtemperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }



    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}
