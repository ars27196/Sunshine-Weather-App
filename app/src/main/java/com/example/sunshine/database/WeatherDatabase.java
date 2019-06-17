package com.example.sunshine.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(version = 1, exportSchema = false, entities = {WeatherData.class})

public abstract class WeatherDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "weather";
    private static WeatherDatabase sInstance;

    public static WeatherDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        WeatherDatabase.class, WeatherDatabase.DATABASE_NAME)
                        //temporary enable
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }
    public abstract WeatherDao myDao();

}
