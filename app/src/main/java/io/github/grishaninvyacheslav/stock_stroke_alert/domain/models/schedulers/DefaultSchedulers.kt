package io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DefaultSchedulers @Inject constructor(): ISchedulers {
    override fun main(): Scheduler = AndroidSchedulers.mainThread()
    override fun background(): Scheduler = Schedulers.io()
}