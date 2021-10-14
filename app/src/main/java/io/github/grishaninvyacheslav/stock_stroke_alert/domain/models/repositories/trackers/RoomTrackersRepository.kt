package io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.trackers

import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Tracker
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request

class RoomTrackersRepository(private val db: TrackersRoomDatabase) : ITrackersRepository {
    private val companyLogoRepository = CompanyLogoRepository()

    override fun getTrackedTickers(): Single<List<Ticker>> =
        Single.fromCallable { return@fromCallable db.trackedTickersDao.getAll() }
            .subscribeOn(Schedulers.io())

    override fun getTickerTrackersCount(symbol: String) = Single.fromCallable {
        return@fromCallable db.trackersDao.getTickerTrackersCount(symbol)
    }.subscribeOn(Schedulers.io())

    override fun getTickerTrackers(symbol: String) = Single.fromCallable {
        return@fromCallable db.trackersDao.getTickerTrackers(symbol)
    }.subscribeOn(Schedulers.io())

    override fun addTracker(tracker: Tracker, trackedTicker: Ticker): Long {
        val id = System.currentTimeMillis();
        Schedulers.io().scheduleDirect {
            db.trackersDao.insert(tracker)
            if(db.trackedTickersDao.getTicker(trackedTicker.symbol) == null){
                trackedTicker.logoUrl = companyLogoRepository.getCompanyLogoUrl(trackedTicker.fullName)
            }
            db.trackedTickersDao.insert(trackedTicker)
        }
        return id
    }

    override fun updateTracker(tracker: Tracker) {
        Schedulers.io().scheduleDirect {
            db.trackersDao.update(tracker)
        }
    }

    override fun delete(tracker: Tracker, trackedTicker: Ticker) {
        Schedulers.io().scheduleDirect {
            db.trackersDao.delete(tracker)
            if (db.trackersDao.getTickerTrackersCount(trackedTicker.symbol) == 0) {
                db.trackedTickersDao.delete(trackedTicker)
            }
        }
    }
}

class CompanyLogoRepository {
    private val client = OkHttpClient()
    private val wordRegex = Regex("\\w+")

    fun getCompanyLogoUrl(fullName: String): String? {
        var logoUrl: String? = null
        var possibleShortName = fullName.toLowerCase().replace(" ", "")
        for (wordMatch in wordRegex.findAll(fullName).toList().asReversed()) {
            val request = Request.Builder()
                .url("https://logo.clearbit.com/${possibleShortName}.com")
                .build();
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    logoUrl = "https://logo.clearbit.com/${possibleShortName}.com"
                    break
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            possibleShortName =
                possibleShortName.replace(Regex("${wordMatch.value.toLowerCase()}$"), "")
        }
        return logoUrl
    }
}