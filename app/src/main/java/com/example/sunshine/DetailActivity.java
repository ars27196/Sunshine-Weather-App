package com.example.sunshine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sunshine.database.WeatherData;

public class DetailActivity extends AppCompatActivity {

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
private Context context;
    private WeatherData mForecast;
    private TextView mWeatherDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
context=this;
        mWeatherDisplay = (TextView) findViewById(R.id.tv_display_weather);
mForecast=Constant.currentSelected;
        mWeatherDisplay.setText(mForecast.getDay()+
                        " "+mForecast.getWeatherType()+" "+mForecast.getWeatherHigh()+
                        " "+mForecast.getWeatherLow()+" ");

    }


    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecast + FORECAST_SHARE_HASHTAG)
                .getIntent();
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