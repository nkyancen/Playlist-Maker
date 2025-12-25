package ru.nkyancen.playlistmaker.player.presentation.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.player.domain.api.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.player.presentation.model.PlayerState

class PlayerViewModel(
    previewUrl: String,
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val handler = Handler(Looper.getMainLooper())

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
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    if (mediaPlayerInteractor.isPlaying()) {
                        playerStateLiveData.postValue(
                            PlayerState.Play(
                                Converter.formatTime(
                                    mediaPlayerInteractor.getCurrentPosition().toLong()
                                )
                            )
                        )

                        handler.postDelayed(
                            this,
                            TIMER_UPDATE_DELAY
                        )
                    } else {
                        handler.removeCallbacks(this)

                        if (mediaPlayerInteractor.isPrepared()) {
                            playerStateLiveData.postValue(
                                PlayerState.Prepared(
                                    Converter.formatTime(0L)
                                )
                            )
                        }
                    }
                }
            },
            TIMER_UPDATE_DELAY
        )
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 500L
    }
}