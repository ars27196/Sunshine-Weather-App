package com.example.sunshine.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.sunshine.sync.SunshineSyncIntentService;

public class SunshinesyncUtils {


    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, SunshineSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
