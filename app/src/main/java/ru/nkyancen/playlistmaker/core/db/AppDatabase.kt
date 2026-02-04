package ru.nkyancen.playlistmaker.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.FavoriteTrackEntity
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.sources.local.db.dao.FavoritesTrackDao
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.entity.PlaylistEntity
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.sources.local.db.dao.PlaylistDao
import ru.nkyancen.playlistmaker.playlist_detail.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.playlist_detail.data.sources.local.db.dao.PlaylistTracksDao

@Database(
    version = 1,
    entities = [FavoriteTrackEntity::class, PlaylistEntity::class, TrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesTrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun playlistTracksDao(): PlaylistTracksDao
}