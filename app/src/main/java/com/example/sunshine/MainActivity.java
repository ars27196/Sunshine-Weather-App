package com.example.sunshine;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunshine.Utilities.NetworkUtils;
import com.example.sunshine.Utilities.OpenWeatherJsonUtils;
import com.example.sunshine.Utilities.SunshinesyncUtils;
import com.example.sunshine.data.SunshinePreferences;
import com.example.sunshine.database.WeatherDao;
import com.example.sunshine.database.WeatherData;
import com.example.sunshine.database.WeatherDatabase;
import com.example.sunshine.sync.SunshineSyncTask;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ForecastAdapter.ForecastAdapterOnClickHandler {

    // COMPLETED (1) Create a field to store the weather display TextView
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private ForecastAdapter mForecastAdapter;
    private RecyclerView mRecyclerView;
    private static final int FORECAST_LOADER_ID = 0;
    private WeatherDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);

        SunshinesyncUtils.startImmediateSync(this);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mForecastAdapter = new ForecastAdapter(this);
        mRecyclerView.setAdapter(mForecastAdapter);
        database = WeatherDatabase.getInstance(getApplicationContext());
        LiveData<List<WeatherData>> getWeather = database.myDao().retrieveWeather();
        getWeather.observe(this, new Observer<List<WeatherData>>() {
            @Override
            public void onChanged(@Nullable List<WeatherData> weatherData) {
                mForecastAdapter.setWeatherData(weatherData);
                mRecyclerView.setAdapter(mForecastAdapter);            }
        });
//        mForecastAdapter.setWeatherData(getWeather);
//        mRecyclerView.setAdapter(mForecastAdapter);


    }


    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showWeatherDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            invalidateData();
            SunshinesyncUtils.startImmediateSync(this);
//            mForecastAdapter.setWeatherData(weather);
//            mRecyclerView.setAdapter(mForecastAdapter);
            return true;
        }
        if (id == R.id.action_map) {
            openLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void invalidateData() {
        mForecastAdapter.setWeatherData(null);
    }

    @Override
    public void onClick(WeatherData weatherForDay) {
        Context context = this;
        Intent intentToStartDetailActivity = new Intent(context, DetailActivity.class);
        Constant.currentSelected = weatherForDay;
        startActivity(intentToStartDetailActivity);
    }

    private void openLocationInMap() {
        String addressString = "Islamabad, PK";
        Uri geoLocation = Uri.parse("geo:0,0?q=" + addressString);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Couldn't call " + geoLocation.toString()
                    + ", no receiving apps installed!", Toast.LENGTH_SHORT)
                    .show();
        }
    }


}