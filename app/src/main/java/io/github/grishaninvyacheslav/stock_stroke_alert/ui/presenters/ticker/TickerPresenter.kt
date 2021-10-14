package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker

import android.graphics.Color
import android.graphics.Paint
import com.github.mikephil.charting.data.*
import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.stock_stroke_alert.App
import io.github.grishaninvyacheslav.stock_stroke_alert.R
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Quote
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Tracker
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.ITickersRepository
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.Interval
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.alphavantage.GlobalQuote
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.trackers.ITrackersRepository
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.schedulers.ISchedulers
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.IScreens
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import moxy.MvpPresenter
import java.lang.StringBuilder
import java.util.*
import javax.inject.Inject

class TickerPresenter(
    private val ticker: Ticker,
    val disposables: CompositeDisposable = CompositeDisposable()
) : MvpPresenter<TickerView>() {
    private val intradayPriceLoadObserver =
        object : DisposableSingleObserver<Map<String, Quote>>() {
            override fun onSuccess(values: Map<String, Quote>) {
                val quotes = values.values.iterator()
                var x = 0F
                val entries: List<CandleEntry> = List(values.size) {
                    x += 1F
                    return@List with(quotes.next()) {
                        CandleEntry(
                            x,
                            high.toFloat(),
                            low.toFloat(),
                            open.toFloat(),
                            close.toFloat()
                        )
                    }
                }
                chartPresenter.setData(entries)
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
            }
        }

    private val globalQuoteLoadObserver = object : DisposableSingleObserver<GlobalQuote>() {
        override fun onSuccess(value: GlobalQuote) {
            viewState.setCurrQuote(value.price)
            viewState.setQuoteChangeValue(value.change.toFloat())
            viewState.setQuoteChangePercent(
                value.changePercent.substring(
                    0,
                    value.changePercent.length - 1
                ).toFloat()
            )
        }

        override fun onError(error: Throwable) {
            error.printStackTrace()
        }
    }

    private fun loadChartData() {
        disposables.add(
            repository
                .intradayPrice(ticker.symbol, Interval.MIN_5)
                .observeOn(schedulers.main())
                .subscribeWith(intradayPriceLoadObserver)
        )
    }

    private fun loadGlobalQuote() {
        disposables.add(
            repository
                .currPrice(ticker.symbol)
                .observeOn(schedulers.main())
                .subscribeWith(globalQuoteLoadObserver)
        )
    }

    private fun editTracker(pos: Int) {
        router.setResultListener(TRACKER_RESULT_KEY) {
            with(it as Tracker) {
                viewState.showTrackerItem(pos)
                trackersRepository.updateTracker(this)
            }
        }
        router.setResultListener(TRACKER_REMOVE_RESULT_KEY) {
            with(it as Tracker) {
                trackersListPresenter.trackers.remove(this)
                viewState.updateTrackersList()
                trackersRepository.delete(this, ticker)
            }
        }
        router.navigateTo(screens.tracker(trackersListPresenter.trackers[pos]))
    }

    inner class ChartPresenter : IChartPresenter {
        private var dataObjects = listOf<CandleEntry>()
        private var cds = CandleDataSet(dataObjects, "Label")
        private var lineData = CandleData(cds)

        fun setData(data: List<CandleEntry>) {
            dataObjects = data
            cds = CandleDataSet(dataObjects, "Label")
            cds.color = Color.rgb(80, 80, 80);
            cds.shadowColor = Color.DKGRAY;
            cds.shadowWidth = 0.7f;
            cds.decreasingColor = Color.RED;
            cds.decreasingPaintStyle = Paint.Style.FILL;
            cds.increasingColor = Color.rgb(122, 242, 84);
            cds.increasingPaintStyle = Paint.Style.FILL;
            cds.neutralColor = Color.BLUE;
            cds.valueTextColor = Color.RED;
            lineData = CandleData(cds)
            viewState.updateChart(lineData)
        }
    }

    inner class TrackersListPresenter : ITrackersListPresenter {
        val trackersLoadObserver = object : DisposableSingleObserver<List<Tracker>>() {
            override fun onSuccess(values: List<Tracker>) {
                trackers.addAll(values)
                viewState.updateTrackersList()
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
            }
        }

        override var itemClickListener: ((TrackerItemView) -> Unit)? = null

        val trackers = mutableListOf<Tracker>()

        override fun getCount() = trackers.size

        override fun bindView(view: TrackerItemView) {
            val tracker = trackers[view.pos]
            with(view) {
                with(tracker) {
                    setTriggerProximity(lastTriggerProximity!!)
                    setDifferenceValue(differenceValue!!)
                    setDifferenceUnits(differenceUnitType!!)
                    setDirection(differenceDirection!!)
                    val timeStringBuilder = StringBuilder()
                    if (days != "0") {
                        timeStringBuilder.append(
                            String.format(
                                app.getString(R.string.time_days),
                                days
                            )
                        )
                    }
                    if (hours != "0") {
                        timeStringBuilder.append(
                            String.format(
                                app.getString(R.string.time_hours),
                                hours
                            )
                        )
                    }
                    if (minutes != "0") {
                        timeStringBuilder.append(
                            String.format(
                                app.getString(R.string.time_minutes),
                                minutes
                            )
                        )
                    }
                    setTime(timeStringBuilder.toString())
                }
            }
        }

        fun loadTrackers() {
            disposables.add(
                trackersRepository
                    .getTickerTrackers(ticker.symbol)
                    .observeOn(schedulers.main())
                    .subscribeWith(trackersLoadObserver)

            )
        }
    }

    val trackersListPresenter = this.TrackersListPresenter()

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

    @Inject
    lateinit var repository: ITickersRepository

    @Inject
    lateinit var trackersRepository: ITrackersRepository

    @Inject
    lateinit var schedulers: ISchedulers

    @Inject
    lateinit var app: App

    val chartPresenter = ChartPresenter()

    companion object {
        // TODO: куда вынести ключи?
        val TRACKER_RESULT_KEY = "TRACKER"
        val TRACKER_REMOVE_RESULT_KEY = "REMOVE_TRACKER"
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        trackersListPresenter.itemClickListener =
            { trackerItemView -> editTracker(trackerItemView.pos) }
        trackersListPresenter.loadTrackers()
        viewState.init()
        loadChartData()
        viewState.setTickerName(ticker.symbol)
        loadGlobalQuote()
    }

    fun createTicker() {
        router.setResultListener(TRACKER_RESULT_KEY) {
            with(it as Tracker) {
                this.databaseId = trackersRepository.addTracker(this, ticker)
                trackersListPresenter.trackers.add(this)
                viewState.updateTrackersList()
                viewState.showTrackerItem(trackersListPresenter.trackers.size - 1)
            }
        }
        router.navigateTo(
            screens.tracker(
                Tracker(
                    trackedTicker = ticker.symbol,
                    lastTriggerProximity = 50
                )
            )
        )
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}