package ru.nkyancen.playlistmaker.medialibrary.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.sources.local.db.dao.FavoritesTrackDao
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.entity.PlaylistEntity
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.sources.local.db.dao.PlaylistDao

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesTrackDao

    abstract fun playlistDao(): PlaylistDao
}