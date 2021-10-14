package io.github.grishaninvyacheslav.stock_stroke_alert.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import io.github.grishaninvyacheslav.stock_stroke_alert.App
import io.github.grishaninvyacheslav.stock_stroke_alert.databinding.FragmentUsersTickersBinding
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.BackButtonListener
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.users_tickers.UsersTickersPresenter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.users_tickers.UsersTickersRVAdapter
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.users_tickers.UsersTickersView
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class UsersTickersFragment : MvpAppCompatFragment(), UsersTickersView, BackButtonListener {
    private val view: FragmentUsersTickersBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    val presenter: UsersTickersPresenter by moxyPresenter {
        UsersTickersPresenter().apply { App.instance.appComponent.inject(this) }
    }

    var adapter: UsersTickersRVAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = view.apply {
        toSearchButton.setOnClickListener{
            presenter.searchButtonPressed()
        }
        presenter.loadUsersTickers()
    }.root

    companion object {
        fun newInstance() = UsersTickersFragment()
    }

    override fun setSearchButtonHint(hint: String) {
        view.toSearchButtonBackground.queryHint = hint
    }

    override fun init() {
        view.usersTickersList.layoutManager = LinearLayoutManager(context)
        adapter = UsersTickersRVAdapter(presenter.usersTickersListPresenter)
        view.usersTickersList.adapter = adapter
    }

    override fun updateUsersTickersList() {
        adapter?.notifyDataSetChanged()
    }

    override fun backPressed() = presenter.backPressed()
}