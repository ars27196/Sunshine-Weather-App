package com.example.sunshine.sync;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class SunshineSyncIntentService extends IntentService {
    public SunshineSyncIntentService() {
        super(SunshineSyncIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SunshineSyncTask.syncWeather(getApplicationContext());
    }
}
