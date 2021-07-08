package com.uhi5d.spotibud.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uhi5d.spotibud.model.SearchHistory
import com.uhi5d.spotibud.model.TrackFinderTracks

@Database(
    entities = [SearchHistory::class, TrackFinderTracks::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun shDao(): SearchHistoryDao
    abstract fun tftDao(): TrackFinderDao

    companion object {
        val DB_NAME = "db_app"
    }
}
