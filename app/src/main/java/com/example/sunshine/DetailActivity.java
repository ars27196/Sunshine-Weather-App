package com.example.sunshine;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sunshine.database.WeatherData;
import com.example.sunshine.databinding.ActivityDetailBinding;
import com.example.sunshine.utilities.SunshineDateUtils;
import com.example.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private Context context;
    private WeatherData mForecast;
    private ActivityDetailBinding mDetailBinding;
    private String mForecastSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        context = this;

        mForecast = Constant.currentSelected;
        int weatherId = mForecast.getWeatherId();

        //setting image icon
        int weatherImageId = SunshineWeatherUtils.getLargeArtResourceIdForWeatherCondition(weatherId);
        mDetailBinding.primaryInfo.weatherIcon.setImageResource(weatherImageId);
        //setting date
        long localDateMidnightGmt = Long.parseLong(mForecast.getDay());
        String dateText = SunshineDateUtils.getFriendlyDateString(this, localDateMidnightGmt, true);
        mDetailBinding.primaryInfo.date.setText(dateText);

        //setting description
        String description = SunshineWeatherUtils.getStringForWeatherCondition(this, weatherId);
        mDetailBinding.primaryInfo.weatherDescription.setText(description);

        //setting high and low temperature
        double highInCelsius = Double.parseDouble(mForecast.getWeatherHigh());
        String highString = SunshineWeatherUtils.formatTemperature(this, highInCelsius);
        mDetailBinding.primaryInfo.highTemperature.setText(highString);
        double lowInCelsius = Double.parseDouble(mForecast.getWeatherLow());
        String lowString = SunshineWeatherUtils.formatTemperature(this, lowInCelsius);
        mDetailBinding.primaryInfo.lowTemperature.setText(lowString);

        //
        float humidity = Float.parseFloat(mForecast.getHumidity());
        String humidityString = getString(R.string.format_humidity, humidity);

        mDetailBinding.extraDetails.humidity.setText(humidityString);

        //setting wind speed and direction
        float windDirection = 0;
        float windSpeed = Float.parseFloat(mForecast.getWindSpeed());
        if(mForecast.getWindDirection()!=null){
             windDirection = Float.parseFloat(mForecast.getWindDirection());
        }

        String windString = SunshineWeatherUtils.getFormattedWind(this, windSpeed, windDirection);
        mDetailBinding.extraDetails.windMeasurement.setText(windString);

        //pressure
        float pressure =Float.parseFloat(mForecast.getPressure());
        String pressureString = getString(R.string.format_pressure, pressure);


        mDetailBinding.extraDetails.pressure.setText(pressureString);

        mForecastSummary = String.format("%s - %s - %s/%s",
                dateText, description, highString, lowString);
    }


    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        MenuItem menuItem2 = menu.findItem(R.id.action_settings);
        menuItem.setIntent(createShareForecastIntent());
        menuItem2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent setting = new Intent(context, SettingsActivity.class);
                startActivity(setting);
                return true;
            }
        });
        return true;
    }
}