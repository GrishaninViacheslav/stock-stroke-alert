package io.github.grishaninvyacheslav.stock_stroke_alert

import android.app.Application
import com.github.mikephil.charting.utils.Utils
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.modules.AppComponent
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.modules.AppModule
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.modules.DaggerAppComponent

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}