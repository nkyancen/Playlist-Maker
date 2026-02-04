package ru.nkyancen.playlistmaker.playlist_detail.data.sources.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nkyancen.playlistmaker.playlist_detail.data.entity.TrackEntity

@Dao
interface PlaylistTracksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToTable(trackEntity: TrackEntity)

    @Query("SELECT * FROM tracks_table WHERE id=:id")
    suspend fun getTrackById(id: Long): TrackEntity

    @Query("DELETE FROM tracks_table WHERE id=:id")
    suspend fun deleteTrackById(id: Long)
}