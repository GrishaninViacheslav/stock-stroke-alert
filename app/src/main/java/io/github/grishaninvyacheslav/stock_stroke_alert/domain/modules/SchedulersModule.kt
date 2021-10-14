package io.github.grishaninvyacheslav.stock_stroke_alert.domain.modules

import dagger.Binds
import dagger.Module
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.schedulers.DefaultSchedulers
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.schedulers.ISchedulers
import javax.inject.Singleton

@Module
interface SchedulersModule {
    @Singleton
    @Binds
    fun bindSchedulers(schedulers: DefaultSchedulers): ISchedulers
}