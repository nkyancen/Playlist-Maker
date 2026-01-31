package ru.nkyancen.playlistmaker.medialibrary.playlists.data.impl

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.entity.PlaylistEntity
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.sources.local.db.dao.PlaylistDao
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistRepository
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist

class PlaylistRepositoryImpl(
    private val dao: PlaylistDao,
    private val entityMapper: PlaylistMapper<PlaylistEntity>,
    private val gson: Gson
) : PlaylistRepository {
    override suspend fun savePlaylistToStorage(playlist: Playlist) {
        dao.addPlaylistToTable(entityMapper.mapFromDomain(playlist))
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val listOfPlaylists = dao.getAllPlaylists()

        emit(
            listOfPlaylists.map {
                entityMapper.mapToDomain(it)
            }
        )
    }

    override fun getTracksIdFromPlaylist(playlist: Playlist): List<Long> {
        val tracksIdString = playlist.listOfTracks

        return try {
            gson.fromJson(tracksIdString, Array<Long>::class.java).toList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun addTrackIdToPlaylist(trackId: Long, playlistId: Long) {
        val playlist = dao.getPlaylistsById(playlistId)

        val listOfTracks = try {
            gson.fromJson(playlist.listOfTracksId, Array<Long>::class.java).toMutableList()
        } catch (_: Exception) {
            mutableListOf()
        }

        listOfTracks.add(trackId)

        dao.updatePlaylist(
            playlist.id,
            gson.toJson(listOfTracks.toList()),
            playlist.tracksAmount + 1
        )
    }
}