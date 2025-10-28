package ru.nkyancen.playlistmaker.domain.player.api

interface MediaPlayerInteractor {
    fun prepare(url: String?)

    fun playbackControl()

    fun start()

    fun pause()

    fun release()

    fun getCurrentPosition(): Int

    fun isPlaying(): Boolean

    fun isPrepared(): Boolean
}