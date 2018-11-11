package com.kayali_developer.objectsrecognition.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.kayali_developer.objectsrecognition.data.model.Object;

@Database(entities = {Object.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final java.lang.Object LOCK = new java.lang.Object();
    private static final String DATABASE_NAME = "objects.db";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract ObjectDao objectDao();
}
