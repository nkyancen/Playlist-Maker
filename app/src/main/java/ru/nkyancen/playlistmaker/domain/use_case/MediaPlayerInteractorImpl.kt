package ru.nkyancen.playlistmaker.domain.use_case

import ru.nkyancen.playlistmaker.domain.repository.media_player.MediaPlayerInteractor
import ru.nkyancen.playlistmaker.domain.repository.media_player.MediaPlayerRepository

class MediaPlayerInteractorImpl(
    private val playerRepository: MediaPlayerRepository
) : MediaPlayerInteractor {
    override fun prepare(url: String?) {
        playerRepository.preparePlayer(url)
    }

    override fun playbackControl() {
        playerRepository.playbackControl()
    }

    override fun start() {
        playerRepository.playerStart()
    }

    override fun pause() {
        playerRepository.playerPause()
    }

    override fun release() {
        playerRepository.releasePlayer()
    }

    override fun getCurrentPosition(): Int = playerRepository.getCurrentPosition()

    override fun isPlaying(): Boolean = playerRepository.isPlayerPlaying()

    override fun isPrepared(): Boolean = playerRepository.isPlayerPrepared()
}