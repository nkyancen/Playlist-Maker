package ru.nkyancen.playlistmaker.player.domain.api

interface MediaPlayerRepository {
    fun preparePlayer(url: String?)

    fun playbackControl()

    fun playerStart()

    fun playerPause()

    fun releasePlayer()

    fun getCurrentPosition(): Int

    fun isPlayerPlaying(): Boolean

    fun isPlayerPrepared(): Boolean
}