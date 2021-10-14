package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.ticker_search

import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.stock_stroke_alert.App
import io.github.grishaninvyacheslav.stock_stroke_alert.R
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.Ticker
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.tickers.ITickersRepository
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.schedulers.ISchedulers
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.Screens
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import moxy.MvpPresenter
import javax.inject.Inject

class TickerSearchPresenter(
    val disposables: CompositeDisposable = CompositeDisposable()
) :
    MvpPresenter<TickerSearchView>() {
    private var currSuggestionDisposables: CompositeDisposable = CompositeDisposable()

    @Inject
    lateinit var repository: ITickersRepository

    @Inject
    lateinit var schedulers: ISchedulers

    val suggestionsListPresenter: TickerSuggestionsListPresenter =
        this.TickerSuggestionsListPresenter()

    inner class TickerSuggestionsListPresenter : ITickerSuggestionsListPresenter {
        private val suggestionsLoadObserver = object : DisposableSingleObserver<List<Ticker>>() {
            override fun onSuccess(value: List<Ticker>) {
                initialSuggestions.addAll(value)
                currSuggestionDisposables.add(
                    Single.just(value)
                        .subscribeOn(schedulers.background())
                        .observeOn(schedulers.main())
                        .subscribeWith(InitialSuggestionsObserver())
                )
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
            }
        }

        private inner class InitialSuggestionsObserver : DisposableSingleObserver<List<Ticker>>() {
            override fun onSuccess(value: List<Ticker>) {
                if (currCharText.isNotEmpty()) {
                    return
                }
                currSuggestions.addAll(value)
                viewState.updateSuggestions()
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
            }
        }

        private inner class CurrSuggestionsObserver : DisposableSingleObserver<List<Ticker>>() {
            override fun onSuccess(value: List<Ticker>) {
                currSuggestions.addAll(value)
                viewState.updateSuggestions()
            }

            override fun onError(error: Throwable) {
                error.printStackTrace()
            }
        }

        private var currCharText: String = ""

        val initialSuggestions = mutableListOf<Ticker>()
        val currSuggestions = mutableListOf<Ticker>()

        override var itemClickListener: ((SuggestionItemView) -> Unit)? = null

        fun loadSuggestions() {
            disposables.add(
                repository
                    .getInitialSuggestions()
                    .observeOn(schedulers.main())
                    .subscribeWith(suggestionsLoadObserver)
            )
        }

        override fun filterSuggestions(charText: String) {
            this.currCharText = charText
            currSuggestions.clear()
            currSuggestionDisposables.dispose()
            currSuggestionDisposables = CompositeDisposable()
            if (charText.isEmpty()) {
                currSuggestions.addAll(initialSuggestions)
            } else {
                currSuggestionDisposables.add(
                    repository
                        .symbolSearch(charText)
                        .observeOn(schedulers.main())
                        .subscribeWith(CurrSuggestionsObserver())
                )
                disposables.add(currSuggestionDisposables)
            }
        }

        override fun getCount() = currSuggestions.size

        override fun bindView(view: SuggestionItemView) {
            val suggestion = currSuggestions[view.pos]
            with(view) {
                setSymbol(suggestion.symbol)
                setFullName(suggestion.fullName)
            }
        }
    }

    @Inject
    lateinit var router: Router

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setQueryHint(App.instance.getString(R.string.ticket_search_hint))
        viewState.init()
        suggestionsListPresenter.loadSuggestions()
        suggestionsListPresenter.itemClickListener = { itemView ->
            router.navigateTo(Screens().ticker(suggestionsListPresenter.currSuggestions[itemView.pos]))
        }
    }

    fun backPressed(): Boolean {
        router.exit()
        return true
    }
}