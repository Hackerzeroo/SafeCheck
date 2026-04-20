package com.example.safecheck;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {SafetyCheck.class, Defect.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract SafetyCheckDao safetyCheckDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "safecheck_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}