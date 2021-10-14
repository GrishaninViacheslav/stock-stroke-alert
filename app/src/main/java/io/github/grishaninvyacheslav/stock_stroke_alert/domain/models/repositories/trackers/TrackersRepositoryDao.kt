package io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.trackers

import androidx.room.*
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Tracker

@Dao
interface TrackersRepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tracker: Tracker)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg trackers: Tracker)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trackers: List<Tracker>)

    @Update
    fun update(tracker: Tracker)

    @Update
    fun update(vararg trackers: Tracker)

    @Update
    fun update(trackers: List<Tracker>)

    @Delete
    fun delete(tracker: Tracker)

    @Delete
    fun delete(vararg trackers: Tracker)

    @Delete
    fun delete(trackers: List<Tracker>)

    @Query("SELECT * FROM tracker")
    fun getAll(): List<Tracker>

    @Query("SELECT COUNT(*) FROM tracker WHERE trackedTicker = :symbol")
    fun getTickerTrackersCount(symbol: String): Int

    @Query("SELECT * FROM tracker WHERE trackedTicker = :symbol")
    fun getTickerTrackers(symbol: String): List<Tracker>

    @Query("SELECT * FROM tracker WHERE databaseId = :hashSum LIMIT 1")
    fun getTrackerByHash(hashSum: String): Tracker
}