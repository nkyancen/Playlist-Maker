package ru.nkyancen.playlistmaker.presentation.player.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.nkyancen.playlistmaker.core.creator.Creator
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.presentation.player.model.PlayerState

class PlayerViewModel(private val url: String) : ViewModel() {
    private val mediaPlayerInteractor = Creator.providePlayerInteractor()

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val progressTimeLiveData = MutableLiveData(Converter.formatTime(0L))
    fun observeProgressTime(): LiveData<String> = progressTimeLiveData

    private val handler = Handler(Looper.getMainLooper())

    init {
        mediaPlayerInteractor.prepare(url)
        playerStateLiveData.postValue(PlayerState.Prepared)
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

    fun onPause(){
        pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayerInteractor.release()
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
        const val TIMER_UPDATE_DELAY = 200L

        fun getFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(url)
            }
        }
    }
}