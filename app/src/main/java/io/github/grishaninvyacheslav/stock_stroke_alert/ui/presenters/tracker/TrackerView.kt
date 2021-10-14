package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.tracker

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface TrackerView : MvpView {
    fun setDaysValue(value: String)
    fun setHoursValue(value: String)
    fun setMinutesValue(value: String)
    fun showDifferenceValueAsInvalid()
    fun showMinutesValueAsInvalid()
    fun showMessage(message: String, isError: Boolean)
}