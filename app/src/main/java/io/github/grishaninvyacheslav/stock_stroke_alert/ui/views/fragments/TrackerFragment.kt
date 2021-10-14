package io.github.grishaninvyacheslav.stock_stroke_alert.ui.views.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.grishaninvyacheslav.stock_stroke_alert.App
import io.github.grishaninvyacheslav.stock_stroke_alert.R
import io.github.grishaninvyacheslav.stock_stroke_alert.databinding.FragmentTrackerBinding
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.NotificationType
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Tracker
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.UnitType
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.BackButtonListener
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.tracker.TrackerPresenter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.tracker.TrackerView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class TrackerFragment : MvpAppCompatFragment(), TrackerView, BackButtonListener {
    private val view: FragmentTrackerBinding by viewBinding(createMethod = CreateMethod.INFLATE)
    private val tracker: Tracker by lazy { arguments?.getParcelable<Tracker>(TRACKER_ARG) as Tracker }
    private val isTrackerHasTimeLimit: Boolean
        get() = view.timePicker.visibility == VISIBLE
    private val isTrackerNew: Boolean
        get() = tracker.differenceValue == null

    val presenter: TrackerPresenter by moxyPresenter {
        TrackerPresenter(tracker).apply {
            App.instance.appComponent.inject(
                this
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = view.apply {
        tracker.differenceValue?.let { differenceValue.text = SpannableStringBuilder(it) }
        differenceValue.setOnFocusChangeListener { _, _ ->
            differenceValue.background.setTint(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
            differenceValue.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            differenceValue.hint = getString(R.string.default_decimal_value)
            if (differenceValue.text.toString() == getString(R.string.default_decimal_value)) {
                view.differenceValue.text = SpannableStringBuilder("")
            }
        }
        if (presenter.days != getString(R.string.default_decimal_value)) {
            days.text = SpannableStringBuilder(presenter.days)
        }
        days.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                presenter.setDays(days.text)
            }
        }
        if (presenter.hours != getString(R.string.default_decimal_value)) {
            hours.text = SpannableStringBuilder(presenter.hours)
        }
        hours.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                presenter.setHours(hours.text)
            }
        }
        if (presenter.minutes != getString(R.string.default_decimal_value)) {
            minutes.text = SpannableStringBuilder(presenter.minutes)
        }
        minutes.setOnFocusChangeListener { _, hasFocus ->
            minutes.background.setTint(ContextCompat.getColor(requireContext(), R.color.grey))
            minutes.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.grey))
            minutes.hint = getString(R.string.default_decimal_value)
            if (!hasFocus) {
                presenter.setMinutes(minutes.text)
            }
        }
        buttonChangeBy.setOnClickListener {
            if (!isTrackerHasTimeLimit) {
                timePicker.visibility = VISIBLE
                presenter.changeByMode()
                val buttonRisePrevState = buttonRise.isChecked
                val buttonFallPrevState = buttonFall.isChecked
                directionSwitch.isSingleSelection = false
                buttonRise.isChecked = buttonRisePrevState
                buttonFall.isChecked = buttonFallPrevState
            }
        }
        buttonCrossingValue.setOnClickListener {
            if (isTrackerHasTimeLimit) {
                timePicker.visibility = GONE
                presenter.crossingValueMode()
                val buttonRisePrevState = buttonRise.isChecked
                val buttonFallPrevState = buttonFall.isChecked
                directionSwitch.isSingleSelection = true
                if (!buttonRisePrevState && buttonFallPrevState) {
                    buttonFall.isChecked = true
                } else {
                    buttonRise.isChecked = true
                }
            }
        }
        buttonRise.isChecked = true
        buttonFall.isChecked = true
        buttonConfirm.text =
            if (isTrackerNew) getString(R.string.create) else getString(R.string.edit)
        buttonConfirm.setOnClickListener {
            presenter.setDifferenceValue(differenceValue.text.toString())
            if (buttonCurrency.isChecked) {
                presenter.setUnitType(UnitType.CURRENCY)
            } else if (buttonPercentage.isCheckable) {
                presenter.setUnitType(UnitType.PERCENTAGE)
            }
            if (buttonRise.isChecked) {
                if (isTrackerHasTimeLimit && buttonFall.isChecked) {
                    presenter.setDirection(0)
                } else {
                    presenter.setDirection(1)
                }
            }
            if (buttonFall.isChecked) {
                if (isTrackerHasTimeLimit && buttonRise.isChecked) {
                    presenter.setDirection(0)
                } else {
                    presenter.setDirection(-1)
                }
            }
            presenter.setThreshold(threshold.text)
            if (checkboxPush.isChecked) {
                presenter.addNotification(NotificationType.PUSH)
            }
            if (checkboxAlarm.isChecked) {
                presenter.addNotification(NotificationType.ALARM)
            }
            if (checkboxMessenger.isChecked) {
                presenter.addNotification(NotificationType.MESSENGER)
            }

            presenter.createTracker()
        }
        buttonDelete.text =
            if (isTrackerNew) getString(R.string.cancel) else getString(R.string.delete)
        buttonDelete.setOnClickListener { presenter.removeTracker() }
    }.root

    companion object {
        private const val TRACKER_ARG = "TRACKER"

        fun newInstance(tracker: Tracker) = TrackerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(
                    TRACKER_ARG, tracker
                )
            }
        }
    }

    override fun setDaysValue(value: String) {
        if (value == getString(R.string.default_decimal_value)) {
            view.days.hint = getString(R.string.default_decimal_value)
            view.days.text = SpannableStringBuilder("")
        } else {
            // TODO: почему нельзя передать просто String как обычному EditText?
            view.days.text = SpannableStringBuilder(value)
        }
    }

    override fun setHoursValue(value: String) {
        if (value == getString(R.string.default_decimal_value)) {
            view.hours.hint = getString(R.string.default_decimal_value)
            view.hours.text = SpannableStringBuilder("")
        } else {
            view.hours.text = SpannableStringBuilder(value)
        }
    }

    override fun setMinutesValue(value: String) {
        if (value == getString(R.string.default_decimal_value)) {
            view.minutes.hint = getString(R.string.default_decimal_value)
            view.minutes.text = SpannableStringBuilder("")
        } else {
            view.minutes.text = SpannableStringBuilder(value)
        }
    }

    override fun showDifferenceValueAsInvalid() {
        view.differenceValue.background.setTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.red
            )
        )
        view.differenceValue.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        view.differenceValue.hint = getString(R.string.invalid_value_hint)
    }

    override fun showMinutesValueAsInvalid() {
        view.minutes.background.setTint(ContextCompat.getColor(requireContext(), R.color.red))
        view.minutes.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.red))
        view.minutes.hint = getString(R.string.invalid_value_hint)
    }

    override fun showMessage(message: String, isError: Boolean) {
        if (isError) {
            view.message.setTextColor(Color.RED)
        }
        view.message.text = message
    }

    override fun backPressed() = presenter.backPressed()
}