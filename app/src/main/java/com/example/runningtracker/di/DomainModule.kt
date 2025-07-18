package com.example.runningtracker.di

import com.example.runningtracker.data.repository.running_tracker.RunningTrackerRepositoryImpl
import com.example.runningtracker.data.repository.running_tracker.locale_db.RunningTrackerDbRepositoryImpl
import com.example.runningtracker.data.repository.running_tracker_detail.locale_db.RunningTrackerDetailDbRepositoryImpl
import com.example.runningtracker.data.repository.running_tracker_history.locale_db.RunningTrackerHistoryDbRepositoryImpl
import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerRepository
import com.example.runningtracker.domain.repository.running_tracker.locale_db.RunningTrackerDbRepository
import com.example.runningtracker.domain.repository.running_tracker_detail.locale_db.RunningTrackerDetailDbRepository
import com.example.runningtracker.domain.repository.running_tracker_history.locale_db.RunningTrackerHistoryDbRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    //region Repository
    @Binds
    @Singleton
    fun bindsRunningTrackerRepository(
        impl: RunningTrackerRepositoryImpl
    ): RunningTrackerRepository

    @Binds
    @Singleton
    fun bindsRunningTrackerDbRepository(
        impl: RunningTrackerDbRepositoryImpl
    ): RunningTrackerDbRepository

    @Binds
    @Singleton
    fun bindsRunningTrackerHistoryDbRepository(
        impl: RunningTrackerHistoryDbRepositoryImpl
    ): RunningTrackerHistoryDbRepository

    @Binds
    @Singleton
    fun bindsRunningTrackerDetailDbRepository(
        impl: RunningTrackerDetailDbRepositoryImpl
    ): RunningTrackerDetailDbRepository
    //endregion
}