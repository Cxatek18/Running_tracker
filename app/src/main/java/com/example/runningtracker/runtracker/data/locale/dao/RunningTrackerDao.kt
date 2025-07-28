package com.example.runningtracker.runtracker.data.locale.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.runningtracker.runtracker.data.locale.module.PointTrackDbModule
import com.example.runningtracker.runtracker.data.locale.module.RunTrackerDbModule
import com.example.runningtracker.runtracker.data.locale.module.RunTrackerWithPointsDbModule
import kotlinx.coroutines.flow.Flow

@Dao
interface RunningTrackerDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveRunTracker(runTracker: RunTrackerDbModule): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPointTrack(point: PointTrackDbModule)

    @Query("SELECT * FROM running_tracker")
    suspend fun getListRunTracker(): List<RunTrackerWithPointsDbModule>

    @Query("SELECT * FROM running_tracker WHERE id = :idRunTracker LIMIT 1")
    suspend fun getRunTracker(idRunTracker: Int): RunTrackerWithPointsDbModule?

    @Query("SELECT * FROM running_tracker WHERE id = :idRunTracker LIMIT 1")
    fun observeRunTracker(idRunTracker: Int): Flow<RunTrackerWithPointsDbModule?>

    @Query("DELETE FROM running_tracker WHERE id = :idRunTracker")
    suspend fun deleteRunTracker(idRunTracker: Int)

    @Query("UPDATE running_tracker SET timeTrack = :time WHERE id = :trackId")
    suspend fun updateTimeTrack(trackId: Int, time: Int)

    @Query("UPDATE running_tracker SET distanceTrack = :distance WHERE id = :trackId")
    suspend fun updateDistanceTrack(trackId: Int, distance: String)
}


