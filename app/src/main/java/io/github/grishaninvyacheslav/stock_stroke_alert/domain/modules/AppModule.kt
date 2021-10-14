package io.github.grishaninvyacheslav.stock_stroke_alert.domain.modules

import dagger.Module
import dagger.Provides
import io.github.grishaninvyacheslav.stock_stroke_alert.App

@Module
class AppModule(val app: App) {
    @Provides
    fun provideApp(): App {
        return app
    }
}