package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.users_tickers

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface UsersTickersView : MvpView {
    fun setSearchButtonHint(hint: String)
    fun init()
    fun updateUsersTickersList()
}