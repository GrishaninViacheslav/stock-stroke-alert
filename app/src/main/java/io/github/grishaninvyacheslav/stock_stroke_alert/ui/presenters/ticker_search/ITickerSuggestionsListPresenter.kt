package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker_search

import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.IListPresenter

interface ITickerSuggestionsListPresenter : IListPresenter<SuggestionItemView> {
    fun filterSuggestions(charText: String)
}