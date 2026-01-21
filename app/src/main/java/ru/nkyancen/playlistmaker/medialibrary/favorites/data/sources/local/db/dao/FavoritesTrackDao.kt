package ru.nkyancen.playlistmaker.medialibrary.favorites.data.sources.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.TrackEntity

@Dao
interface FavoritesTrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackEntity: TrackEntity)

    @Delete
    suspend fun deleteTrack(trackEntity: TrackEntity)

    @Query("SELECT * FROM favorites_table ORDER by createdTime DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT id FROM favorites_table")
    suspend fun getTracksId(): List<Long>
}