package io.github.grishaninvyacheslav.stock_stroke_alert.domain.modules

import androidx.room.Room
import dagger.Module
import dagger.Provides
import io.github.grishaninvyacheslav.stock_stroke_alert.App
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.trackers.ITrackersRepository
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.trackers.RoomTrackersRepository
import io.github.grishaninvyacheslav.stock_stroke_alert.domain.models.repositories.trackers.TrackersRoomDatabase
import javax.inject.Singleton

@Module
class RoomTrackersRepositoryModule {
    @Singleton
    @Provides
    fun trackersDatabase(app: App): TrackersRoomDatabase =
        Room.databaseBuilder(app, TrackersRoomDatabase::class.java, TrackersRoomDatabase.DB_NAME)
            .build()

    @Singleton
    @Provides
    fun trackersRepository(database: TrackersRoomDatabase): ITrackersRepository = RoomTrackersRepository(database)
}