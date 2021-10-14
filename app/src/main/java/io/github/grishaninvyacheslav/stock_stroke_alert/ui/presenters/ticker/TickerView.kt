package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker

import com.github.mikephil.charting.data.CandleData
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TickerView : MvpView {
    fun init()
    fun setTickerName(name: String)
    fun setCurrQuote(quote: String)
    fun setQuoteChangeValue(change: Float)
    fun setQuoteChangePercent(percent: Float)
    fun updateChart(data: CandleData)
    fun updateTrackersList()
    fun showTrackerItem(position: Int)
}