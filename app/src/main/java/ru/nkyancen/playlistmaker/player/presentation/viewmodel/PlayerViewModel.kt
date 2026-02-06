package ru.nkyancen.playlistmaker.player.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.core.utils.SingleLiveEvent
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistCoverInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem
import ru.nkyancen.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.player.presentation.model.PlayerBottomSheetState
import ru.nkyancen.playlistmaker.player.presentation.model.PlayerState
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class PlayerViewModel(
    private val trackId: Long,
    previewUrl: String,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val itemMapper: TrackMapper<TrackItem>,
    private val playlistInteractor: PlaylistInteractor,
    private val playlistItemMapper: PlaylistMapper<PlaylistItem>,
    private val playlistCoverInteractor: PlaylistCoverInteractor
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val bottomSheetLiveData =
        MutableLiveData<PlayerBottomSheetState>(PlayerBottomSheetState.Hide)

    fun observeBottomSheetState(): LiveData<PlayerBottomSheetState> = bottomSheetLiveData

    private val showMessageLiveData = SingleLiveEvent<Pair<String, Boolean>>()
    fun observeShowMessage(): LiveData<Pair<String, Boolean>> = showMessageLiveData

    private val favoritesTracksId = mutableListOf<Long>()

    private var timeJob: Job? = null

    init {
        mediaPlayerInteractor.prepare(previewUrl)
        viewModelScope.launch {
            favoritesInteractor
                .getFavoriteTracksId()
                .collect {
                    favoritesTracksId.addAll(it)
                }

            renderState(
                PlayerState.Prepared(
                    Converter.formatTime(0L),
                    isFavorites()
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()
    }

    fun onPlayButtonClicked() {
        when (playerStateLiveData.value) {
            is PlayerState.Play -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Pause -> startPlayer()
            else -> pausePlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.start()
        provideTimer()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pause()
        timeJob?.cancel()
        renderState(
            PlayerState.Pause(
                Converter.formatTime(
                    mediaPlayerInteractor.getCurrentPosition().toLong()
                ),
                isFavorites()
            )
        )
    }

    fun onPause() {
        pausePlayer()
    }

    private fun provideTimer() {
        timeJob?.cancel()
        timeJob = viewModelScope.launch {
            while (mediaPlayerInteractor.isPlaying()) {
                delay(TIMER_UPDATE_DELAY)
                playerStateLiveData.setValue(
                    PlayerState.Play(
                        Converter.formatTime(
                            mediaPlayerInteractor.getCurrentPosition().toLong()
                        ),
                        isFavorites()
                    )
                )
            }

            if (mediaPlayerInteractor.isPrepared()) {
                playerStateLiveData.setValue(
                    PlayerState.Pause(
                        Converter.formatTime(0L),
                        isFavorites()
                    )
                )
            }
        }
    }

    fun onFavoriteButtonClicked(track: TrackItem) {
        if (playerStateLiveData.value?.isFavorites ?: false) {
            deleteFromFavorites()
        } else {
            addToFavorites(track)
        }
    }

    fun isFavorites(): Boolean = favoritesTracksId.contains(trackId)

    private fun processFavorites(isFavorites: Boolean): PlayerState {
        val currentProgressTime = playerStateLiveData.value?.progressTime ?: "00:00"

        return when (playerStateLiveData.value) {
            is PlayerState.Pause -> PlayerState.Pause(currentProgressTime, isFavorites)
            is PlayerState.Play -> PlayerState.Play(currentProgressTime, isFavorites)
            is PlayerState.Prepared -> PlayerState.Prepared(currentProgressTime, isFavorites)
            else -> PlayerState.Prepared(currentProgressTime, isFavorites)
        }
    }

    private fun addToFavorites(track: TrackItem) {
        favoritesTracksId.add(trackId)

        viewModelScope.launch {
            favoritesInteractor.insertTrackToFavorites(
                itemMapper.mapToDomain(track)
            )

            renderState(
                processFavorites(true)
            )
        }
    }

    private fun deleteFromFavorites() {
        favoritesTracksId.remove(trackId)

        viewModelScope.launch {
            favoritesInteractor.deleteTrackFromFavorites(
                trackId
            )

            renderState(
                processFavorites(false)
            )
        }
    }

    fun getUriForCover(coverName: String): Uri? =
        if (coverName.isNotEmpty()) {
            playlistCoverInteractor.loadImageFromStorage(coverName)
        } else {
            null
        }

    fun onPlaylistClick(
        track: TrackItem,
        playlist: PlaylistItem,
        coroutineExec: CompletedCoroutineExec
    ) {
        val tracksIdList = playlistInteractor.getTracksIdFromPlaylist(
            playlistItemMapper.mapToDomain(playlist)
        )

        if (trackId in tracksIdList) {
            showMessageLiveData.setValue(Pair(playlist.title, false))
        } else {

            renderBottomSheetState(PlayerBottomSheetState.Hide)

            showMessageLiveData.postValue(Pair(playlist.title, true))
            viewModelScope.launch {
                coroutineExec.afterCompletion(
                    playlistInteractor.addTrackToPlaylist(
                        itemMapper.mapToDomain(track),
                        playlistItemMapper.mapToDomain(playlist)
                    ).single()
                )
            }
        }
    }


    fun showBottomSheet() {
        viewModelScope.launch {
            playlistInteractor
                .getAllPlaylists()
                .collect { playlists ->
                    renderBottomSheetState(
                        PlayerBottomSheetState.Show(
                            playlistItemMapper.mapListFromDomain(
                                playlists
                            )
                        )
                    )
                }
        }
    }

    fun hideBottomSheet() {
        renderBottomSheetState(PlayerBottomSheetState.Hide)
    }

    private fun renderState(state: PlayerState) {
        playerStateLiveData.postValue(state)
    }

    private fun renderBottomSheetState(state: PlayerBottomSheetState) {
        bottomSheetLiveData.postValue(state)
    }

    fun interface CompletedCoroutineExec {
        fun afterCompletion(result: Any)
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
    }
}