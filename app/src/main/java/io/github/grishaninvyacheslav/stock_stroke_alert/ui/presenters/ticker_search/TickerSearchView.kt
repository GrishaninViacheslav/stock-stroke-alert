package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker_search

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TickerSearchView : MvpView {
    fun init()
    fun updateSuggestions()
    fun setQueryHint(hint: String)
}