package com.example.sunshine.sync;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.example.sunshine.utilities.NetworkUtils;
import com.example.sunshine.utilities.OpenWeatherJsonUtils;
import com.example.sunshine.data.SunshinePreferences;
import com.example.sunshine.database.WeatherData;
import com.example.sunshine.database.WeatherDatabase;

import java.net.URL;
import java.util.List;

public class SunshineSyncTask {
    public static  void syncWeather(Context context) {

        String locationQuery = SunshinePreferences
                .getPreferredWeatherLocation(context);

        URL weatherRequestUrl = NetworkUtils.getUrl(context);

        try {
            String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);

            List<WeatherData> weatherData = OpenWeatherJsonUtils
                    .getWeatherListFromJson(context, jsonWeatherResponse);

            if (weatherData != null && weatherData.size() != 0) {
                WeatherDatabase.getInstance(context).myDao().deleteWeatherItems();

                for (WeatherData weatherdata : weatherData) {
                    WeatherDatabase.getInstance(context).myDao().addWeather(weatherdata);

                }
            }

           // return simpleJsonWeatherData;
        } catch (Exception e) {
            Toast.makeText(context,"error: "+e,Toast.LENGTH_LONG).show();
            e.printStackTrace();
           //3 return null;
        }
    }
}
