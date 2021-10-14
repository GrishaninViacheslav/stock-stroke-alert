package io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.main

import com.github.terrakok.cicerone.Router
import io.github.grishaninvyacheslav.stock_stroke_alert.ui.presenters.IScreens
import moxy.MvpPresenter
import javax.inject.Inject

class MainPresenter() : MvpPresenter<MainView>() {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var screens: IScreens

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(screens.usersTickers())
    }

    fun backClicked() {
        router.exit()
    }
}