package ru.nkyancen.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.player.presentation.model.PlayerState

class PlayerViewModel(
    previewUrl: String,
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private var timeJob: Job? = null

    init {
        mediaPlayerInteractor.prepare(previewUrl)
        playerStateLiveData.postValue(PlayerState.Prepared(Converter.formatTime(0L)))
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
        playerStateLiveData.postValue(
            PlayerState.Pause(
                Converter.formatTime(
                    mediaPlayerInteractor.getCurrentPosition().toLong()
                )
            )
        )
    }

    fun onPause() {
        pausePlayer()
    }

    private fun provideTimer() {
        timeJob = viewModelScope.launch {
            while (mediaPlayerInteractor.isPlaying()) {
                delay(TIMER_UPDATE_DELAY)
                playerStateLiveData.postValue(
                    PlayerState.Play(
                        Converter.formatTime(
                            mediaPlayerInteractor.getCurrentPosition().toLong()
                        )
                    )
                )
            }

            if (mediaPlayerInteractor.isPrepared()) {
                playerStateLiveData.postValue(
                    PlayerState.Pause(
                        Converter.formatTime(0L)
                    )
                )
            }
        }
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 300L
    }
}