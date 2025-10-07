package ru.nkyancen.playlistmaker.domain.repository.media_player

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