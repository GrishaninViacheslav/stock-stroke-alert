package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import io.github.grishaninvyacheslav.stock_stroke_alert.R
import io.github.grishaninvyacheslav.stock_stroke_alert.databinding.ItemTickerTrackerBinding

class TrackersRVAdapter(
    private val presenter: ITrackersListPresenter,
) : RecyclerView.Adapter<TrackersRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemTickerTrackerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ).apply {
            itemView.findViewById<MaterialButton>(R.id.button_edit).setOnClickListener { presenter.itemClickListener?.invoke(this) }
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        presenter.bindView(holder.apply { pos = position })

    override fun getItemCount() = presenter.getCount()

    inner class ViewHolder(val vb: ItemTickerTrackerBinding) : RecyclerView.ViewHolder(vb.root),
        TrackerItemView {
        override var pos = -1

        override fun setTriggerProximity(proximity: Byte) {
            vb.triggerProximity.text = proximity.toString()
        }

        override fun setDifferenceValue(value: String) {
            vb.difference.text = value
        }

        override fun setDifferenceUnits(unit: String) {
            // TODO: Используя Spans задать оформление для units
            vb.difference.text =
                "${vb.difference.text}$unit"
        }

        override fun setDirection(direction: Byte) {
            if (direction > 0) {
                vb.directionUp.visibility = VISIBLE
                vb.directionDown.visibility = GONE
            } else if (direction < 0) {
                vb.directionUp.visibility = GONE
                vb.directionDown.visibility = VISIBLE
            } else {
                vb.directionUp.visibility = VISIBLE
                vb.directionDown.visibility = VISIBLE
            }
        }

        override fun setTime(time: String) {
            vb.time.text = time
        }
    }
}