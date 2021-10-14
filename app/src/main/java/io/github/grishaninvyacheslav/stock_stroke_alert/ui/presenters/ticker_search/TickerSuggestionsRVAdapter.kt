package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.grishaninvyacheslav.stock_stroke_alert.databinding.ListViewItemsBinding

class TickerSuggestionsRVAdapter(
    private val presenter: ITickerSuggestionsListPresenter,
) : RecyclerView.Adapter<TickerSuggestionsRVAdapter.ViewHolder>() {
    fun filter(charText: String) {
        presenter.filterSuggestions(charText)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListViewItemsBinding.inflate(
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

    inner class ViewHolder(val vb: ListViewItemsBinding) : RecyclerView.ViewHolder(vb.root),
        SuggestionItemView {
        override var pos = -1

        override fun setSymbol(symbol: String) {
            vb.tvTicket.text = symbol
        }

        override fun setFullName(fullName: String) {
            vb.tvCompanyFullName.text = fullName
        }
    }
}