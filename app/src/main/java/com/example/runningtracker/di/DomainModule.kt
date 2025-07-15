package com.example.runningtracker.di

import com.example.runningtracker.data.repository.running_tracker.RunningTrackerRepositoryImpl
import com.example.runningtracker.domain.repository.running_tracker.RunningTrackerRepository
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
    //endregion
}