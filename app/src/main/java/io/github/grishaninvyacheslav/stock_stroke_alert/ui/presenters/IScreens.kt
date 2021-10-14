package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters

import com.github.terrakok.cicerone.Screen
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Tracker

interface IScreens {
    fun usersTickers(): Screen
    fun tickerSearch(): Screen
    fun ticker(ticker: Ticker): Screen
    fun tracker(tracker: Tracker): Screen
}