package ru.nkyancen.playlistmaker.utils

import android.media.MediaPlayer

class MediaPlayerService {

    private var mediaPlayer = MediaPlayer()
    var playerState = PlayerState.DEFAULT
        private set

    fun playbackControl() {
        if (playerState == PlayerState.PLAYING) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun preparePlayer(url: String?) {
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PREPARED
        }
        setOnCompletionListener()
    }

    fun setOnCompletionListener() {
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.PREPARED
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }

    fun releasePlayer() {
        mediaPlayer.release()
    }

    fun getCurrentPosition(): Int = mediaPlayer.currentPosition

    enum class PlayerState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }
}
