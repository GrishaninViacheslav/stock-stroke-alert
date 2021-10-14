package io.github.grishaninvyacheslav.stock_stroke_alert.domain.modules

import dagger.Binds
import dagger.Module
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.ITickersRepository
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.alphavantage.TickerRepository
import javax.inject.Singleton

@Module
interface AlphaVantageRepositoryModule {
    @Singleton
    @Binds
    fun bindRepository(tickerRepository: TickerRepository): ITickersRepository
}