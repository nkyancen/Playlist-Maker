package ru.nkyancen.playlistmaker.medialibrary.favorites.data.sources.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.sources.local.db.dao.FavoritesTrackDao

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesTrackDao
}