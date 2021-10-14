package io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.alphavantage

import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Quote
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.ITickersRepository
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.Interval
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class TickerRepository @Inject constructor(private val tickerDataApi: IAlphaVantageDataSource) :
    ITickersRepository {
    override fun symbolSearch(keywords: String): Single<List<Ticker>> =
        tickerDataApi.symbolSearch(keywords)
            .flatMap { bestMatchesArr -> Single.just(bestMatchesArr["bestMatches"]!!) }
            .subscribeOn(Schedulers.io())


    override fun getInitialSuggestions(): Single<List<Ticker>> = symbolSearch("a")

    override fun currPrice(symbol: String) =
        tickerDataApi.globalQuote(symbol)
            .flatMap { value -> Single.just(value["Global Quote"]!!) }
            .subscribeOn(Schedulers.io())


    override fun intradayPrice(symbol: String, interval: Interval): Single<Map<String, Quote>> =
        tickerDataApi.intraday(symbol, "5min")
            .flatMap { value ->
                var intradayPriceSeries = value.timeSeries
                intradayPriceSeries = intradayPriceSeries.toSortedMap().toMap()
                Single.just(intradayPriceSeries)
            }.subscribeOn(Schedulers.io())
}