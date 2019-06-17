package com.example.sunshine.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "weather")
public class WeatherData {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String day;
    private String weatherType;
    private String weatherHigh;
    private String weatherLow;
    private String pressure;
    private String humidity;
    private String windSpeed,windDirection;
private int weatherId;
    @Ignore
    public WeatherData() {

    }

    @Ignore
    public WeatherData(String day, String weatherType, String weatherHigh, String weatherLow) {
        this.day = day;
        this.weatherType = weatherType;
        this.weatherHigh = weatherHigh;
        this.weatherLow = weatherLow;
    }

    public WeatherData(int id, String day, String weatherType, String weatherHigh, String weatherLow, String pressure, String humidity, String windSpeed, String windDirection, int weatherId) {
        this.id = id;
        this.day = day;
        this.weatherType = weatherType;
        this.weatherHigh = weatherHigh;
        this.weatherLow = weatherLow;
        this.pressure = pressure;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.weatherId = weatherId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherHigh() {
        return weatherHigh;
    }

    public void setWeatherHigh(String weatherHigh) {
        this.weatherHigh = weatherHigh;
    }

    public String getWeatherLow() {
        return weatherLow;
    }

    public void setWeatherLow(String weatherLow) {
        this.weatherLow = weatherLow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }
}
