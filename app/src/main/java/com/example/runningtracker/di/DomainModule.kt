package com.example.runningtracker.di

import com.example.runningtracker.detail.data.repository.RunningTrackerDetailDbRepositoryImpl
import com.example.runningtracker.detail.domain.repository.RunningTrackerDetailDbRepository
import com.example.runningtracker.history.data.repository.RunningTrackerHistoryDbRepositoryImpl
import com.example.runningtracker.history.domain.repository.RunningTrackerHistoryDbRepository
import com.example.runningtracker.runtracker.data.repository.RunningTrackerDbRepositoryImpl
import com.example.runningtracker.runtracker.domain.repository.RunningTrackerDbRepository
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