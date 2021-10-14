package io.github.grishaninvyacheslav.stock_stroke_alert.ui

import com.github.terrakok.cicerone.androidx.FragmentScreen
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Tracker
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.IScreens
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.views.fragments.TickerFragment
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.views.fragments.TickerSearchFragment
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.views.fragments.TrackerFragment
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.views.fragments.UsersTickersFragment

class Screens : IScreens {
    override fun usersTickers() = FragmentScreen { UsersTickersFragment.newInstance() }
    override fun tickerSearch() = FragmentScreen { TickerSearchFragment.newInstance() }
    override fun ticker(ticker: Ticker) = FragmentScreen { TickerFragment.newInstance(ticker) }
    override fun tracker(tracker: Tracker) = FragmentScreen { TrackerFragment.newInstance(tracker) }
}