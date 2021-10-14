package io.github.grishaninvyacheslav.stock_stroke_alert.domain.modules

import dagger.Component
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.main.MainPresenter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker.TickerPresenter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker_search.TickerSearchPresenter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.tracker.TrackerPresenter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.users_tickers.UsersTickersPresenter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.views.MainActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        CiceroneModule::class,
        AlphaVantageApiModule::class,
        AlphaVantageRepositoryModule::class,
        SchedulersModule::class,
        RoomTrackersRepositoryModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)

    fun inject(mainPresenter: MainPresenter)
    fun inject(tickerSearchPresenter: TickerSearchPresenter)
    fun inject(usersTickersPresenter: UsersTickersPresenter)
    fun inject(tickerPresenter: TickerPresenter)
    fun inject(trackerPresenter: TrackerPresenter)
}