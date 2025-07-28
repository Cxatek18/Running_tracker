package com.example.runningtracker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.runningtracker.runtracker.data.locale.database.RunningTrackerDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    //region Running tracker database
    @Singleton
    @Provides
    fun provideRunningTrackerDatabase(
        @ApplicationContext context: Context
    ): RunningTrackerDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = RunningTrackerDatabase::class.java,
            name = "running_tracker.db"
        ).build()
    }
    //endregion

    //region  Shared preferences
    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences =
        context.getSharedPreferences("RunTrackerPreferences", Context.MODE_PRIVATE)
    //endregion
}