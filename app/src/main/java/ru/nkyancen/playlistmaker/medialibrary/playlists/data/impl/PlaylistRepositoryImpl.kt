package ru.nkyancen.playlistmaker.medialibrary.playlists.data.impl

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.entity.PlaylistEntity
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.sources.local.db.dao.PlaylistDao
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistRepository
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist
import ru.nkyancen.playlistmaker.playlist_detail.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.playlist_detail.data.sources.local.db.dao.PlaylistTracksDao
import ru.nkyancen.playlistmaker.search.domain.models.Track

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistTracksDao: PlaylistTracksDao,
    private val playlistEntityMapper: PlaylistMapper<PlaylistEntity>,
    private val trackEntityMapper: TrackMapper<TrackEntity>,
    private val gson: Gson
) : PlaylistRepository {
    override fun savePlaylistToStorage(playlist: Playlist): Flow<Boolean> = flow {
        playlistDao.addPlaylistToTable(playlistEntityMapper.mapFromDomain(playlist))

        emit(
            true
        )
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val listOfPlaylists = playlistDao.getAllPlaylists()

        emit(
            listOfPlaylists.map {
                playlistEntityMapper.mapToDomain(it)
            }
        )
    }

    override fun getTracksIdList(playlist: Playlist): List<Long> = try {
        gson.fromJson(playlist.listOfTracks, Array<Long>::class.java).toList()
    } catch (_: Exception) {
        emptyList()
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> = flow {
        val playlist = playlistDao.getPlaylistsById(playlistId)

        emit(
            playlistEntityMapper.mapToDomain(playlist)
        )
    }

    override fun addTrackToPlaylist(track: Track, playlist: Playlist): Flow<Boolean> = flow {

        val listOfTracksId = try {
            gson.fromJson(playlist.listOfTracks, Array<Long>::class.java).toMutableList()
        } catch (_: Exception) {
            mutableListOf()
        }

        playlistTracksDao.addTrackToTable(
            trackEntityMapper.mapFromDomain(track)
        )

        listOfTracksId.add(track.id)

        playlistDao.updatePlaylistContent(
            playlist.id, gson.toJson(listOfTracksId.toList()), playlist.tracksAmount + 1
        )

        emit(
            true
        )
    }

    override fun deleteTrackFromPlaylist(
        trackId: Long, playlist: Playlist
    ): Flow<Boolean> = flow {
        val listOfTracksId = getTracksIdList(playlist).toMutableList()

        listOfTracksId.remove(trackId)

        playlistDao.updatePlaylistContent(
            playlist.id, gson.toJson(listOfTracksId.toList()), playlist.tracksAmount - 1
        )

        deleteTracksWithoutPlaylist(trackId)

        emit(
            true
        )
    }

    private suspend fun deleteTracksWithoutPlaylist(trackId: Long) {
        val listOfPlaylist = playlistDao.getAllPlaylists()

        if (!isTrackInSomePlaylist(trackId, listOfPlaylist)) {
            playlistTracksDao.deleteTrackById(trackId)
        }
    }

    private fun isTrackInSomePlaylist(
        trackId: Long, listOfPlaylists: List<PlaylistEntity>
    ): Boolean = listOfPlaylists.any {
        trackId in getTracksIdList(
            playlistEntityMapper.mapToDomain(it)
        )
    }

    override fun deletePlaylistById(playlistId: Long): Flow<Boolean> = flow {
        val playlist = playlistEntityMapper.mapToDomain(playlistDao.getPlaylistsById(playlistId))

        val tracksIdList = getTracksIdList(
            playlist
        )

        playlistDao.deletePlaylistById(playlistId)

        tracksIdList.map { trackId ->
            deleteTracksWithoutPlaylist(trackId)
        }

        emit(
            true
        )
    }

    override fun updatePlaylistInfo(playlistInfo: Playlist): Flow<Boolean> = flow {
        playlistDao.updatePlaylistInfo(
            playlistInfo.id,
            playlistInfo.title,
            playlistInfo.coverImage,
            playlistInfo.description
        )

        emit(
            true
        )
    }
}
