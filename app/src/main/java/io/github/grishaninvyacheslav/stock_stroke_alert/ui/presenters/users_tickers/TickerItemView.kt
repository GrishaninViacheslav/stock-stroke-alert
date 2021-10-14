package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.users_tickers

import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.IItemView

interface TickerItemView : IItemView {
    fun setName(name: String)
    fun setCurrQuote(quote: String)
    fun setQuoteChange(change: String)
    fun setTrackersCount(count: String)
    fun setLogo(url: String)
}