package io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.trackers

import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Tracker
import io.reactivex.Single

interface ITrackersRepository {
    fun getTrackedTickers(): Single<List<Ticker>>
    fun getTickerTrackersCount(symbol: String): Single<Int>
    fun getTickerTrackers(symbol: String): Single<List<Tracker>>
    fun addTracker(tracker: Tracker, trackedTicker: Ticker): Long
    fun updateTracker(tracker: Tracker)
    fun delete(tracker: Tracker, trackedTicker: Ticker)
}