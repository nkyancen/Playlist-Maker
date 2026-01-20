package ru.nkyancen.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.domain.api.FavoritesInteractor
import ru.nkyancen.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.player.presentation.model.PlayerState
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class PlayerViewModel(
    private val trackId: Long,
    previewUrl: String,
    private val mediaPlayerInteractor: MediaPlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val itemMapper: TrackMapper<TrackItem>
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val tracksId = mutableListOf<Long>()

    private var timeJob: Job? = null

    init {
        mediaPlayerInteractor.prepare(previewUrl)
        viewModelScope.launch {
            favoritesInteractor
                .getFavoriteTracksId()
                .collect {
                    tracksId.addAll(it)
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
        when (playerStateLiveData.value!!) {
            is PlayerState.Play -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Pause -> startPlayer()
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
                renderState(
                    PlayerState.Play(
                        Converter.formatTime(
                            mediaPlayerInteractor.getCurrentPosition().toLong()
                        ),
                        isFavorites()
                    )
                )
            }

            if (mediaPlayerInteractor.isPrepared()) {
                renderState(
                    PlayerState.Pause(
                        Converter.formatTime(0L),
                        isFavorites()
                    )
                )
            }
        }
    }

    fun onFavoriteButtonClicked(track: TrackItem) {
        if (playerStateLiveData.value?.isFavorites!!) {
            deleteFromFavorites(track)
        } else {
            addToFavorites(track)
        }
    }

    fun isFavorites(): Boolean = tracksId.contains(trackId)

    private fun addToFavorites(track: TrackItem) {
        tracksId.add(trackId)

        viewModelScope.launch {
            favoritesInteractor.insertTrackToFavorites(
                itemMapper.mapToDomain(track)
            )

            renderState(
                playerStateLiveData.value!!.apply {
                    isFavorites = true
                }
            )
        }
    }

    private fun deleteFromFavorites(track: TrackItem) {
        tracksId.remove(trackId)

        viewModelScope.launch {
            favoritesInteractor.deleteTrackFromFavorites(
                itemMapper.mapToDomain(track)
            )

            renderState(
                playerStateLiveData.value!!.apply {
                    isFavorites = false
                }
            )
        }
    }

    private fun renderState(state: PlayerState) {
        playerStateLiveData.postValue(state)
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
    }
}