package io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers

import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import io.reactivex.Single

interface ITickersInitialSuggestionsRepository {
    fun getInitialSuggestions(): Single<List<Ticker>>
}