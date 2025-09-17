package ru.nkyancen.playlistmaker.common

import android.media.MediaPlayer

object MediaPlayer {

    private var mediaPlayer = MediaPlayer()
    var playerState = State.DEFAULT
        private set

    fun playbackControl() {
        if (playerState == State.PLAYING) {
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
            playerState = State.PREPARED
        }
        setOnCompletionListener()
    }

    fun setOnCompletionListener() {
        mediaPlayer.setOnCompletionListener {
            playerState = State.PREPARED
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        playerState = State.PLAYING
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState = State.PAUSED
    }

    fun releasePlayer() {
        mediaPlayer.release()
    }

    fun getCurrentPosition() = mediaPlayer.currentPosition

    enum class State {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
    }
}
