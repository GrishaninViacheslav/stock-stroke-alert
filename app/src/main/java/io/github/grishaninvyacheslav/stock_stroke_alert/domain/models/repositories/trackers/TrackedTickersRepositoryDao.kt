package io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.trackers

import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import androidx.room.*

@Dao
interface TrackedTickersRepositoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(ticker: Ticker)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(vararg ticker: Ticker)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(tickers: List<Ticker>)

    @Update
    fun update(ticker: Ticker)

    @Update
    fun update(vararg tickers: Ticker)

    @Update
    fun update(tickers: List<Ticker>)

    @Delete
    fun delete(ticker: Ticker)

    @Delete
    fun delete(vararg tickers: Ticker)

    @Delete
    fun delete(tickers: List<Ticker>)

    @Query("SELECT * FROM ticker WHERE symbol = :symbol LIMIT 1")
    fun getTicker(symbol: String): Ticker?

    @Query("SELECT * FROM Ticker")
    fun getAll(): List<Ticker>
}