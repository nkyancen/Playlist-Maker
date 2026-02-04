package ru.nkyancen.playlistmaker.playlist_detail.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.core.utils.SingleLiveEvent
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistCoverInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem
import ru.nkyancen.playlistmaker.playlist_detail.domain.api.PlaylistDetailInteractor
import ru.nkyancen.playlistmaker.playlist_detail.presentation.model.PlaylistDetailsBottomSheetState
import ru.nkyancen.playlistmaker.playlist_detail.presentation.model.PlaylistDetailsMenuBottomSheetState
import ru.nkyancen.playlistmaker.playlist_detail.presentation.model.PlaylistDetailsState
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem
import ru.nkyancen.playlistmaker.settings.domain.api.ExternalNavigator

class PlaylistDetailsViewModel(
    private val playlistId: Long,
    private val playlistItemMapper: PlaylistMapper<PlaylistItem>,
    private val trackItemMapper: TrackMapper<TrackItem>,
    private val playlistInteractor: PlaylistInteractor,
    private val playlistCoverInteractor: PlaylistCoverInteractor,
    private val playlistDetailInteractor: PlaylistDetailInteractor,
    private val externalNavigator: ExternalNavigator
) : ViewModel() {

    private val playlistDetailsLiveData =
        MutableLiveData<PlaylistDetailsState>(PlaylistDetailsState.Loading)

    fun observePlaylistDetailsState(): LiveData<PlaylistDetailsState> = playlistDetailsLiveData

    private val playlistDetailsBottomSheetLiveData =
        MutableLiveData<PlaylistDetailsBottomSheetState>(PlaylistDetailsBottomSheetState.Loading)

    fun observePlaylistDetailsBottomSheetState(): LiveData<PlaylistDetailsBottomSheetState> =
        playlistDetailsBottomSheetLiveData

    private val playlistDetailsMenuBottomSheetLiveData =
        MutableLiveData<PlaylistDetailsMenuBottomSheetState>(PlaylistDetailsMenuBottomSheetState.Hide)

    fun observePlaylistDetailsMenuBottomSheetState(): LiveData<PlaylistDetailsMenuBottomSheetState> =
        playlistDetailsMenuBottomSheetLiveData

    private val showMessageLiveData = SingleLiveEvent<Any?>()
    fun observeShowMessage(): LiveData<Any?> = showMessageLiveData


    fun getUriForCover(coverName: String): Uri? = if (coverName.isNotEmpty()) {
        playlistCoverInteractor.loadImageFromStorage(coverName)
    } else {
        null
    }

    fun updatePlaylistDetails() {

        renderState(PlaylistDetailsState.Loading)
        renderBottomSheetState(PlaylistDetailsBottomSheetState.Loading)

        viewModelScope.launch {
            playlistInteractor
                .getPlaylistById(playlistId)
                .collect { playlist ->
                    getTotalDuration(playlistItemMapper.mapFromDomain(playlist))

                    getTrackList(playlistItemMapper.mapFromDomain(playlist))
                }
        }
    }

    private suspend fun getTrackList(playlist: PlaylistItem) {
        playlistDetailInteractor.getAllTracksFromPlaylist(
            playlistItemMapper.mapToDomain(playlist)
        ).collect {
            if (it.isNotEmpty()) {
                renderBottomSheetState(
                    PlaylistDetailsBottomSheetState.Content(
                        tracks = trackItemMapper.mapListFromDomain(it)
                    )
                )
            } else {
                renderBottomSheetState(
                    PlaylistDetailsBottomSheetState.Empty
                )
            }
        }
    }

    private suspend fun getTotalDuration(playlist: PlaylistItem) {
        playlistDetailInteractor.getTotalProgressTimeByPlaylist(
            playlistItemMapper.mapToDomain(playlist)
        ).collect {
            renderState(
                PlaylistDetailsState.Content(
                    playlist = playlist, totalDuration = it
                )
            )
        }
    }

    fun deleteTrackFromPlaylist(track: TrackItem) {
        renderState(PlaylistDetailsState.Loading)
        renderBottomSheetState(PlaylistDetailsBottomSheetState.Loading)

        viewModelScope.launch {
            playlistInteractor
                .getPlaylistById(playlistId)
                .collect { playlist ->
                    playlistInteractor.deleteTrackFromPlaylist(
                        track.id,
                        playlist
                    ).single()
                }

            playlistInteractor
                .getPlaylistById(playlistId)
                .collect { playlist ->
                    getTotalDuration(
                        playlistItemMapper.mapFromDomain(playlist)
                    )

                    getTrackList(
                        playlistItemMapper.mapFromDomain(playlist)
                    )
                }
        }
    }

    fun deletePlaylist(playlistId: Long, coroutineExec: CompletedCoroutineExec) {
        viewModelScope.launch {
            coroutineExec.afterCompletion(
                playlistInteractor.deletePlaylistById(playlistId).single()
            )
        }
    }

    fun sharePlaylist(playlistId: Long) {
        hideMenu()

        viewModelScope.launch {

            playlistInteractor.getPlaylistById(playlistId)
                .collect { playlist ->
                    if (playlist.tracksAmount == 0) {
                        showMessageLiveData.setValue(null)
                    } else {
                        playlistDetailInteractor
                            .getPlaylistContent(playlist)
                            .collect {
                                externalNavigator.shareText(it)
                            }
                    }
                }
        }
    }

    fun showMenu() {
        renderMenuBottomSheetState(
            PlaylistDetailsMenuBottomSheetState.Show
        )
    }

    fun hideMenu() {
        renderMenuBottomSheetState(
            PlaylistDetailsMenuBottomSheetState.Hide
        )
    }

    private fun renderState(state: PlaylistDetailsState) {
        playlistDetailsLiveData.postValue(state)
    }

    private fun renderBottomSheetState(state: PlaylistDetailsBottomSheetState) {
        playlistDetailsBottomSheetLiveData.postValue(state)
    }

    private fun renderMenuBottomSheetState(state: PlaylistDetailsMenuBottomSheetState) {
        playlistDetailsMenuBottomSheetLiveData.setValue(state)
    }

    fun interface CompletedCoroutineExec {
        fun afterCompletion(result: Any)
    }
}