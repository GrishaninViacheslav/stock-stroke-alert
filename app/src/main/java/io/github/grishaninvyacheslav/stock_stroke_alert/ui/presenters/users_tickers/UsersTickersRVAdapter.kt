package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.users_tickers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.grishaninvyacheslav.stock_stroke_alert.App
import io.github.grishaninvyacheslav.stock_stroke_alert.databinding.ItemUsersTickerBinding

class UsersTickersRVAdapter(
    private val presenter: IUsersTickersListPresenter,
) : RecyclerView.Adapter<UsersTickersRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemUsersTickerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.setOnClickListener { presenter.itemClickListener?.invoke(this) }
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        presenter.bindView(holder.apply { pos = position })

    override fun getItemCount() = presenter.getCount()

    inner class ViewHolder(val view: ItemUsersTickerBinding) : RecyclerView.ViewHolder(view.root),
        TickerItemView {
        override var pos = -1

        override fun setName(name: String) {
            view.symbol.text = name
        }

        override fun setCurrQuote(quote: String) {
            view.currQuote.text = quote
        }

        override fun setQuoteChange(change: String) {
            view.quoteChange.text = change
        }

        override fun setTrackersCount(count: String) {
            view.notificationsCount.text = count
        }

        override fun setLogo(url: String) {
            Glide.with(App.instance)
                .load(url)
                .into(view.companyLogo)
        }
    }
}