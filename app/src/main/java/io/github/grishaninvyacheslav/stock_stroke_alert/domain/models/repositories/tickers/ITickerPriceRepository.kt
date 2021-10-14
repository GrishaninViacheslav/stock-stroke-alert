package io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers

import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Quote
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.alphavantage.GlobalQuote
import io.reactivex.Single

interface ITickerPriceRepository {
    fun currPrice(symbol: String): Single<GlobalQuote>
    fun intradayPrice(symbol: String, interval: Interval): Single<Map<String, Quote>>
}

enum class Interval(value: String) {
    MIN_1("1min"),
    MIN_5("5min"),
    MIN_15("15min"),
    MIN_30("30min"),
    MIN_60("60min")
}