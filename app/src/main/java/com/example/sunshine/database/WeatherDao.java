package com.example.sunshine.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert
    void addWeather(WeatherData weatherData);
    @Query("Select * from weather order by id")
    LiveData<List<WeatherData>> retrieveWeather();
    @Query("DELETE FROM weather")
    void deleteWeatherItems();
}
