package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker

import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.IItemView

interface TrackerItemView : IItemView {
    fun setTriggerProximity(proximity: Byte)
    fun setDifferenceValue(value: String)
    fun setDifferenceUnits(unit: String)
    fun setDirection(direction: Byte)
    fun setTime(time: String)
}