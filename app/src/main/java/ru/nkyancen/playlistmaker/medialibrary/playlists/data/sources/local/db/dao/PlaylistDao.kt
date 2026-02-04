package ru.nkyancen.playlistmaker.medialibrary.playlists.data.sources.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylistToTable(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Query("SELECT * FROM playlist_table WHERE id=:id")
    suspend fun getPlaylistsById(id: Long): PlaylistEntity

    @Query("UPDATE playlist_table SET listOfTracksId = :listOfTracks, tracksAmount = :tracksAmount WHERE id = :id")
    suspend fun updatePlaylistContent(id: Long, listOfTracks: String, tracksAmount: Int)

    @Query("UPDATE playlist_table SET title=:title, coverImage=:coverImage, description=:description WHERE id=:id")
    suspend fun updatePlaylistInfo(id: Long, title: String, coverImage: String, description: String)

    @Query("DELETE FROM playlist_table WHERE id=:id")
    suspend fun deletePlaylistById(id: Long)
}