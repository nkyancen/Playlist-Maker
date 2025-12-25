package ru.nkyancen.playlistmaker.player.data.impl

import android.media.MediaPlayer
import ru.nkyancen.playlistmaker.player.domain.api.MediaPlayerRepository

class MediaPlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer
) : MediaPlayerRepository {

    private var playerState = PlayerState.DEFAULT

    override fun preparePlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.PREPARED
        }
    }

    override fun playbackControl() {
        if (playerState == PlayerState.PLAYING) {
            playerPause()
        } else {
            playerStart()
        }
    }

    override fun playerStart() {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
    }

    override fun playerPause() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition() = mediaPlayer.currentPosition

    override fun isPlayerPlaying() = (playerState == PlayerState.PLAYING)

    override fun isPlayerPrepared() = (playerState == PlayerState.PREPARED)

    companion object {
        private enum class PlayerState {
            DEFAULT,
            PREPARED,
            PLAYING,
            PAUSED
        }
    }
}