package com.example.sunshine;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunshine.data.SunshinePreferences;
import com.example.sunshine.utilities.SunshinesyncUtils;
import com.example.sunshine.database.WeatherData;
import com.example.sunshine.viewModel.WeatherViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ForecastAdapter.ForecastAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    // COMPLETED (1) Create a field to store the weather display TextView
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private ForecastAdapter mForecastAdapter;
    private RecyclerView mRecyclerView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);
        context = this;
        SunshinesyncUtils.startImmediateSync(this);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mForecastAdapter = new ForecastAdapter(this);
        mRecyclerView.setAdapter(mForecastAdapter);
        setupViewModel();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupViewModel() {
        WeatherViewModel viewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);

        viewModel.getmWeatherData().observe(this, new Observer<List<WeatherData>>() {

            @Override
            public void onChanged(@Nullable List<WeatherData> weatherData) {
                mForecastAdapter.setWeatherData(weatherData);
                mRecyclerView.setAdapter(mForecastAdapter);
            }

        });
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

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void invalidateData() {
        mForecastAdapter.setWeatherData(null);
    }

    @Override
    public void onClick(WeatherData weatherForDay) {
        Intent intentToStartDetailActivity = new Intent(context, DetailActivity.class);
        Constant.currentSelected = weatherForDay;
        startActivity(intentToStartDetailActivity);
    }

    private void openLocationInMap() {
        String addressString = SunshinePreferences.getPreferredWeatherLocation(context);
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


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setupViewModel();
        Constant.PREFERENCES_HAVE_BEEN_UPDATED = true;
    }
}