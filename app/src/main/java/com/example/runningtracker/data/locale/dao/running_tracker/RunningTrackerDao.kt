package com.example.runningtracker.data.locale.dao.running_tracker

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.runningtracker.data.locale.module.running_tracker.PointTrackDbModule
import com.example.runningtracker.data.locale.module.running_tracker.RunTrackerDbModule
import com.example.runningtracker.data.locale.module.running_tracker.RunTrackerWithPointsDbModule

@Dao
interface RunningTrackerDao {

    @Query("SELECT * FROM running_tracker")
    suspend fun getListRunTracker(): List<RunTrackerWithPointsDbModule>

    @Query("SELECT * FROM running_tracker WHERE id = :idRunTracker LIMIT 1")
    suspend fun getRunTracker(idRunTracker: Int): RunTrackerWithPointsDbModule?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRunTracker(runTracker: RunTrackerDbModule): Long

    @Query("DELETE FROM running_tracker WHERE id = :idRunTracker")
    suspend fun deleteRunTracker(idRunTracker: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPointTrackList(points: List<PointTrackDbModule>)

    @Query("SELECT * FROM point_track WHERE runId == :runId ORDER BY numberPoint ASC")
    suspend fun getPointsForRun(runId: Int): List<PointTrackDbModule>
}