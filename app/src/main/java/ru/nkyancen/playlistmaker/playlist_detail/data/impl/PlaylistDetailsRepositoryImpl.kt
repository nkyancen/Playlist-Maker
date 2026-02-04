package ru.nkyancen.playlistmaker.playlist_detail.data.impl

import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist
import ru.nkyancen.playlistmaker.playlist_detail.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.playlist_detail.data.sources.local.db.dao.PlaylistTracksDao
import ru.nkyancen.playlistmaker.playlist_detail.domain.api.PlaylistDetailsRepository
import ru.nkyancen.playlistmaker.search.domain.models.Track

class PlaylistDetailsRepositoryImpl(
    private val playlistTracksDao: PlaylistTracksDao,
    private val trackEntityMapper: TrackMapper<TrackEntity>,
    private val gson: Gson,
    private val appContext: Context
) : PlaylistDetailsRepository {
    override fun getAllTracksFromPlaylist(playlist: Playlist): Flow<List<Track>> = flow {
        val tracksList = getTracksList(playlist.listOfTracks)

        emit(
            trackEntityMapper.mapListToDomain(tracksList)
        )
    }

    override fun getTotalProgressTimeByPlaylist(playlist: Playlist): Flow<Int> = flow {
        val tracksList = getTracksList(playlist.listOfTracks)

        var totalDuration = 0L

        tracksList.forEach {
            totalDuration += it.trackTime
        }

        emit(
            Converter.formatTimeByMinutes(totalDuration)
        )
    }

    override fun getPlaylistContentByPlainText(playlist: Playlist): Flow<String> = flow {
        var plainText = "${playlist.title}\n${playlist.description}\n"
        plainText += appContext.resources.getQuantityString(
            R.plurals.track_amount,
            playlist.tracksAmount,
            playlist.tracksAmount
        ) + "\n"

        getTracksList(playlist.listOfTracks)
            .forEachIndexed { index, track ->
                plainText += "${index + 1}. ${track.artistName} - ${track.trackName} (${
                    Converter.formatTime(
                        track.trackTime
                    )
                })\n"
            }

        emit(
            plainText
        )
    }

    private fun convertTracksIdStringToList(string: String): List<Long> = try {
        gson.fromJson(string, Array<Long>::class.java).toList()
    } catch (_: Exception) {
        emptyList()
    }

    private suspend fun getTracksList(listOfTracksByString: String): List<TrackEntity> =
        convertTracksIdStringToList(listOfTracksByString)
            .reversed()
            .map { id ->
            playlistTracksDao.getTrackById(id)
        }
}
