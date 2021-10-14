package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters

interface IListPresenter<V : IItemView> {
    var itemClickListener: ((V) -> Unit)?
    fun bindView(view: V)
    fun getCount(): Int
}