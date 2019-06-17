/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.sunshine;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sunshine.database.WeatherData;
import com.example.sunshine.utilities.SunshineDateUtils;
import com.example.sunshine.utilities.SunshineWeatherUtils;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private List<WeatherData> mWeatherData;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private boolean mUseTodayLayout;
    private final ForecastAdapterOnClickHandler mClickHandler;


    public interface ForecastAdapterOnClickHandler {
        void onClick(WeatherData weatherForDay);
    }


    public ForecastAdapter(ForecastAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }


    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        final ImageView iconView;

        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;

        public ForecastAdapterViewHolder(View view) {
            super(view);
            iconView = (ImageView) view.findViewById(R.id.weather_icon);
            dateView = (TextView) view.findViewById(R.id.date);
            descriptionView = (TextView) view.findViewById(R.id.weather_description);
            highTempView = (TextView) view.findViewById(R.id.high_temperature);
            lowTempView = (TextView) view.findViewById(R.id.low_temperature);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            WeatherData weatherForDay = mWeatherData.get(adapterPosition);
            mClickHandler.onClick(weatherForDay);
        }
    }

    Context context;
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
         context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        WeatherData weatherForThisDay = mWeatherData.get(position);

        forecastAdapterViewHolder.descriptionView.setText(weatherForThisDay.getWeatherType());
        forecastAdapterViewHolder.lowTempView.setText(weatherForThisDay.getWeatherLow());
        forecastAdapterViewHolder.descriptionView.setText(weatherForThisDay.getWeatherType());
        forecastAdapterViewHolder.dateView.setText(weatherForThisDay.getDay());

        //setting weather Icon
        int viewType = getItemViewType(position);
        int weatherImageId;
        int weatherId=mWeatherData.get(position).getWeatherId();

        switch (viewType) {

            case VIEW_TYPE_TODAY:
                weatherImageId = SunshineWeatherUtils
                        .getLargeArtResourceIdForWeatherCondition(weatherId);
                break;

            case VIEW_TYPE_FUTURE_DAY:
                weatherImageId = SunshineWeatherUtils
                        .getSmallArtResourceIdForWeatherCondition(weatherId);
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

        forecastAdapterViewHolder.iconView.setImageResource(weatherImageId);

        long dateInMillis = Long.parseLong(mWeatherData.get(position).getDay());

        //setting date
        String dateString = SunshineDateUtils.getFriendlyDateString(context, dateInMillis, false);
        forecastAdapterViewHolder.dateView.setText(dateString);

        //setting weather description
        String description = SunshineWeatherUtils.getStringForWeatherCondition(context, weatherId);
        forecastAdapterViewHolder.descriptionView.setText(description);

        //setting high and low temperatures

        double highInCelsius = Double.parseDouble(mWeatherData.get(position).getWeatherHigh());
        String highString = SunshineWeatherUtils.formatTemperature(context, highInCelsius);
        forecastAdapterViewHolder.highTempView.setText(highString);

        double lowInCelsius = Double.parseDouble(mWeatherData.get(position).getWeatherLow());
        String lowString = SunshineWeatherUtils.formatTemperature(context, lowInCelsius);
        forecastAdapterViewHolder.lowTempView.setText(lowString);

    }

    @Override
    public int getItemViewType(int position) {

        if ( position == 0) {
            return VIEW_TYPE_TODAY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.size();
    }


    public void setWeatherData(List<WeatherData> weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }
}