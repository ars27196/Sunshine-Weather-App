package com.example.sunshine.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.example.sunshine.database.WeatherData;
import com.example.sunshine.database.WeatherDatabase;

import java.util.List;


public class WeatherViewModel extends AndroidViewModel {

    private LiveData<List<WeatherData>> mWeatherData;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
        WeatherDatabase database=WeatherDatabase.getInstance(application);
        mWeatherData=database.myDao().retrieveWeather();
    }


    public LiveData<List<WeatherData>> getmWeatherData() {
        return mWeatherData;
    }
}
