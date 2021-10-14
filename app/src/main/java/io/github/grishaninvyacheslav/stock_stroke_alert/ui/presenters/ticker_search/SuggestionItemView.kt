package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker_search

import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.IItemView

interface SuggestionItemView : IItemView {
    fun setSymbol(symbol: String)
    fun setFullName(fullName: String)
}