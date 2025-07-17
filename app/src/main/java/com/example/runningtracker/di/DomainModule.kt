package com.example.runningtracker.di

import com.example.runningtracker.data.repository.running_tracker.RunningTrackerRepositoryImpl
import com.example.runningtracker.data.repository.running_tracker.locale_db.RunningTrackerDbRepositoryImpl
import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerRepository
import com.example.runningtracker.domain.repository.running_tracker.locale_db.RunningTrackerDbRepository
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
    //endregion
}