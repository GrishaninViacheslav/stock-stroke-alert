package io.github.grishaninvyacheslav.stock_stroke_alert.ui.views.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mikephil.charting.data.CandleData
import io.github.grishaninvyacheslav.stock_stroke_alert.App
import io.github.grishaninvyacheslav.stock_stroke_alert.R
import io.github.grishaninvyacheslav.stock_stroke_alert.databinding.FragmentTickerBinding
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.BackButtonListener
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker.TickerPresenter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker.TickerView
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker.TrackersRVAdapter
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import kotlin.properties.Delegates

class TickerFragment : MvpAppCompatFragment(), TickerView, BackButtonListener {
    private lateinit var unfoldIcon: Drawable
    private lateinit var foldIcon: Drawable
    private var hideHistoryPreviewButtonIconPadding by Delegates.notNull<Int>()
    private val view: FragmentTickerBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private fun loadResources() {
        unfoldIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.outline_unfold_more_24, null)!!
        foldIcon =
            ResourcesCompat.getDrawable(resources, R.drawable.outline_unfold_less_24, null)!!
    }

    private fun buttonHideHistoryPreviewClickEvent() {
        fun setChartsViewsVisibility(visibility: Int) {
            with(view) {
                chart.visibility = visibility
                chartInteractBlocker.visibility = visibility
                buttonSwitchToCandlestickChart.visibility = visibility
                buttonSwitchToLineChart.visibility = visibility
            }
        }

        fun hideChart() {
            with(view) {
                setChartsViewsVisibility(GONE)
                hideHistoryPreviewButtonIconPadding = buttonHideHistoryPreview.iconPadding
                buttonHideHistoryPreview.iconPadding =
                    -(buttonHideHistoryPreview.icon?.intrinsicWidth ?: 0)
                buttonHideHistoryPreview.text =
                    getString(R.string.button_show_history_preview)
                buttonHideHistoryPreview.icon = unfoldIcon
            }
        }

        fun showChar() {
            with(view) {
                setChartsViewsVisibility(VISIBLE)
                buttonHideHistoryPreview.iconPadding = hideHistoryPreviewButtonIconPadding
                buttonHideHistoryPreview.text =
                    getString(R.string.button_hide_history_preview)
                buttonHideHistoryPreview.icon = foldIcon
            }
        }

        with(view) {
            if (chart.visibility == VISIBLE) {
                hideChart()
            } else {
                showChar()
            }
        }
    }

    val presenter: TickerPresenter by moxyPresenter {
        TickerPresenter(arguments?.getParcelable<Ticker>(TICKER_ARG) as Ticker).apply {
            App.instance.appComponent.inject(
                this
            )
        }
    }

    var adapter: TrackersRVAdapter? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = view.apply {
        backButton.setOnClickListener { presenter.backPressed() }
        buttonHideHistoryPreview.setOnClickListener { buttonHideHistoryPreviewClickEvent() }
        chartInteractBlocker.setOnTouchListener { _, _ -> true }
        buttonCreateTicker.setOnClickListener { presenter.createTicker() }
        buttonToHistory.iconPadding = -(buttonToHistory.icon?.intrinsicWidth ?: 0)
    }.also {
        loadResources()
    }.root

    companion object {
        private const val TICKER_ARG = "TICKER"

        fun newInstance(ticker: Ticker) = TickerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(
                    TICKER_ARG, ticker
                )
            }
        }
    }

    override fun init() {
        view.tickerTrackersList.layoutManager = LinearLayoutManager(context)
        adapter = TrackersRVAdapter(presenter.trackersListPresenter)
        view.tickerTrackersList.adapter = adapter
    }

    override fun setTickerName(name: String) {
        view.tickerName.text = name
    }

    override fun setCurrQuote(quote: String) {
        view.currQuote.text = String.format(getString(R.string.decimal_number_format), quote.toFloat())
    }

    override fun setQuoteChangeValue(change: Float) {
        if (change > 0) {
            view.quoteChange.setTextColor(resources.getColor(R.color.green))
        } else if (change < 0) {
            view.quoteChange.setTextColor(resources.getColor(R.color.red))
        } else {
            view.quoteChange.setTextColor(resources.getColor(R.color.blue))
        }
        view.quoteChange.text = String.format(getString(R.string.decimal_number_format), change)
    }

    override fun setQuoteChangePercent(percent: Float) {
        view.quoteChange.text = String.format(
            getString(R.string.quote_change_with_percent),
            view.quoteChange.text,
            String.format(getString(R.string.decimal_number_format), percent),
            getString(R.string.percent_symbol)
        )
    }

    override fun updateChart(data: CandleData) {
        view.chart.data = data
        view.chart.invalidate()
    }

    override fun updateTrackersList() {
        adapter?.notifyDataSetChanged()
    }

    override fun showTrackerItem(position: Int) {
        view.tickerTrackersList.scrollToPosition(position);
    }

    override fun backPressed() = presenter.backPressed()
}