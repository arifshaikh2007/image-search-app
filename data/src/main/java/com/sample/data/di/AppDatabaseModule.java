package com.sample.data.di;

import android.app.Application;

import androidx.room.Room;

import com.sample.data.db.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppDatabaseModule {
    private static final String DB_NAME = "app_db";
    @Singleton
    @Provides
    public AppDatabase provideAppDatabase(Application app) {
        return Room
                .databaseBuilder(app.getApplicationContext(), AppDatabase.class, DB_NAME)
                .build();
    }
}