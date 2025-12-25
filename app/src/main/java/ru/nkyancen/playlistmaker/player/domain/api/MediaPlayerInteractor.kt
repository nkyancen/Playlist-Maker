package ru.nkyancen.playlistmaker.player.domain.api

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