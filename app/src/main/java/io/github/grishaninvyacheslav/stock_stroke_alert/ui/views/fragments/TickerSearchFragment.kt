package io.github.grishaninvyacheslav.stock_stroke_alert.ui.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.grishaninvyacheslav.stock_stroke_alert.App
import io.github.grishaninvyacheslav.stock_stroke_alert.databinding.FragmentTickerSearchBinding
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.BackButtonListener
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker_search.TickerSuggestionsRVAdapter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker_search.TickerSearchPresenter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker_search.TickerSearchView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class TickerSearchFragment : MvpAppCompatFragment(), BackButtonListener, TickerSearchView,
    SearchView.OnQueryTextListener {
    private val view: FragmentTickerSearchBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private fun showKeyboard() {
        view.search.requestFocus()
        with(requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager) {
            showSoftInput(view.search.findFocus(), 0)
        }
    }

    private fun hideSearchViewIcon() {
        with(resources.getIdentifier("android:id/search_mag_icon", null, null)) {
            view.search.findViewById<ImageView>(this).layoutParams = LinearLayout.LayoutParams(0, 0)
        }
    }

    companion object {
        fun newInstance() = TickerSearchFragment()
    }

    val presenter: TickerSearchPresenter by moxyPresenter {
        TickerSearchPresenter().apply { App.instance.appComponent.inject(this) }
    }
    var adapter: TickerSuggestionsRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = view.apply {
        backButton.setOnClickListener { presenter.backPressed() }
        search.setOnQueryTextListener(this@TickerSearchFragment)
        hideSearchViewIcon()
    }.root

    override fun onResume() {
        super.onResume()
        showKeyboard()
    }

    override fun backPressed() = presenter.backPressed()

    override fun init() {
        view.listview.layoutManager = LinearLayoutManager(context)
        adapter = TickerSuggestionsRVAdapter(presenter.suggestionsListPresenter)
        view.listview.adapter = adapter
    }

    override fun updateSuggestions() {
        adapter?.notifyDataSetChanged()
    }

    override fun setQueryHint(hint: String) {
        view.search.queryHint = hint
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapter?.filter(newText)
        return false
    }
}