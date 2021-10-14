package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.tracker

import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.stock_stroke_alert.App
// TODO: как избавится от R?
import io.github.grishaninvyacheslav.stock_stroke_alert.R
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.NotificationType
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Tracker
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.UnitType
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker.TickerPresenter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker.TickerPresenter.Companion.TRACKER_REMOVE_RESULT_KEY
import moxy.MvpPresenter
import java.lang.StringBuilder
import javax.inject.Inject
import kotlin.math.roundToInt

class TrackerPresenter(private val tracker: Tracker) : MvpPresenter<TrackerView>() {
    private var isTrackingChange = true
    private fun formatTimePicker() {
        val totalMinutes =
            this.days.toFloat() * 1440f + this.hours.toFloat() * 60f + this.minutes.toFloat()
        val formattedDays = (totalMinutes / 1440).toInt()
        val formattedHours = ((totalMinutes - formattedDays * 1440) / 60).toInt()
        val formattedMinutes =
            (totalMinutes - formattedDays * 1440 - formattedHours * 60).roundToInt()

        this.days = formattedDays.toString()
        this.hours = formattedHours.toString()
        this.minutes = formattedMinutes.toString()

        viewState.setDaysValue(formattedDays.toString())
        viewState.setHoursValue(formattedHours.toString())
        viewState.setMinutesValue(formattedMinutes.toString())
    }

    private fun validate(tracker: Tracker): Boolean {
        with(tracker) {
            var isValid = true
            val errorMessageBuilder = StringBuilder()
            if (differenceValue == "") {
                errorMessageBuilder.append(app.getString(R.string.error_value_filed_is_empty))
                viewState.showDifferenceValueAsInvalid()
                isValid = false
            }
            if (isTrackingChange && this@TrackerPresenter.days == "0" && this@TrackerPresenter.hours == "0" && this@TrackerPresenter.minutes == "0") {
                errorMessageBuilder.append("${if (errorMessageBuilder.isEmpty()) "" else "\n"}${app.getString(R.string.error_time_filed_is_empty)}")
                viewState.showMinutesValueAsInvalid()
                isValid = false
            }

            if (!isValid) {
                viewState.showMessage(errorMessageBuilder.toString(), true)
            }
            return isValid
        }
    }

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var app: App

    var days: String = tracker.days ?: "0"
    var hours: String = tracker.hours ?: "0"
    var minutes: String = tracker.minutes ?: "0"

    fun setDifferenceValue(value: CharSequence) {
        tracker.differenceValue = value.toString()
    }

    fun setUnitType(unitType: UnitType) {
        tracker.differenceUnitType = unitType.value
    }

    fun setDirection(direction: Byte) {
        tracker.differenceDirection = direction
    }

    fun setDays(days: CharSequence) {
        this.days = if (days.toString().isEmpty()) "0" else days.toString()
        formatTimePicker()
    }

    fun setHours(hours: CharSequence) {
        this.hours = if (hours.toString().isEmpty()) "0" else hours.toString()
        formatTimePicker()
    }

    fun setMinutes(minutes: CharSequence) {
        this.minutes = if (minutes.toString().isEmpty()) "0" else minutes.toString()
        formatTimePicker()
    }

    fun setThreshold(threshold: CharSequence) {
        tracker.triggerThreshold = threshold.toString()
    }

    fun addNotification(notificationType: NotificationType) {
        if (!tracker.notifications.contains(notificationType.value)) {
            tracker.notifications += "&${notificationType.value}"
        }
    }

    fun createTracker() {
        with(tracker) {
            if (!validate(this)) {
                return
            }
            if (triggerThreshold == "") {
                triggerThreshold = "100"
            }
            days = this@TrackerPresenter.days
            hours = this@TrackerPresenter.hours
            minutes = this@TrackerPresenter.minutes
            router.sendResult(TickerPresenter.TRACKER_RESULT_KEY, this)
        }
        router.exit()
    }

    fun removeTracker() {
        router.sendResult(TRACKER_REMOVE_RESULT_KEY, tracker)
        backPressed()
    }

    fun changeByMode() {
        isTrackingChange = true
    }

    fun crossingValueMode() {
        isTrackingChange = false
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}