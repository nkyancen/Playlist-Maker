package ru.nkyancen.playlistmaker.medialibrary.favorites.data.sources.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.FavoriteTrackEntity

@Dao
interface FavoritesTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(favoriteTrackEntity: FavoriteTrackEntity)

    @Query("DELETE FROM favorites_table WHERE id=:favoriteTrackId")
    suspend fun deleteTrack(favoriteTrackId: Long)

    @Query("SELECT * FROM favorites_table ORDER by createdTime DESC")
    suspend fun getTracks(): List<FavoriteTrackEntity>

    @Query("SELECT id FROM favorites_table")
    suspend fun getTracksId(): List<Long>
}