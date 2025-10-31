package ru.nkyancen.playlistmaker.presentation.player.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.domain.player.api.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.presentation.player.model.PlayerState

class PlayerViewModel(
    private val previewUrl: String,
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val progressTimeLiveData = MutableLiveData(Converter.formatTime(0L))
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private val handler = Handler(Looper.getMainLooper())

    init {
        mediaPlayerInteractor.prepare(previewUrl)
        playerStateLiveData.postValue(PlayerState.Prepared)
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()
    }

    fun onPlayButtonClicked() {
        when (playerStateLiveData.value!!) {
            PlayerState.Play -> pausePlayer()
            PlayerState.Prepared, PlayerState.Pause -> startPlayer()
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractor.start()
        playerStateLiveData.postValue(PlayerState.Play)
        provideTimer()
    }

    private fun pausePlayer() {
        mediaPlayerInteractor.pause()
        playerStateLiveData.postValue(PlayerState.Pause)
    }

    fun onPause() {
        pausePlayer()
    }

    private fun provideTimer() {
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    if (mediaPlayerInteractor.isPlaying()) {
                        progressTimeLiveData.postValue(
                            Converter.formatTime(
                                mediaPlayerInteractor.getCurrentPosition().toLong()
                            )
                        )

                        handler.postDelayed(
                            this,
                            TIMER_UPDATE_DELAY
                        )
                    } else {
                        handler.removeCallbacks(this)

                        if (mediaPlayerInteractor.isPrepared()) {
                            progressTimeLiveData.postValue(Converter.formatTime(0L))
                            playerStateLiveData.postValue(PlayerState.Prepared)
                        }
                    }
                }
            },
            TIMER_UPDATE_DELAY
        )
    }

    companion object {
        private const val TIMER_UPDATE_DELAY = 200L
    }
}