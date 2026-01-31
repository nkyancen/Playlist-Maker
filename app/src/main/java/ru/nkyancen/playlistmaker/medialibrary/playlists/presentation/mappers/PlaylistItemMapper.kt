package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.mappers

import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

class PlaylistItemMapper: PlaylistMapper<PlaylistItem> {
    override fun mapFromDomain(model: Playlist) = PlaylistItem(
        id = model.id,
        title = model.title,
        description = model.description,
        coverImage = model.coverImage,
        listOfTracks = model.listOfTracks,
        tracksAmount = model.tracksAmount
    )

    override fun mapToDomain(dto: PlaylistItem) = Playlist(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        coverImage = dto.coverImage,
        listOfTracks = dto.listOfTracks,
        tracksAmount = dto.tracksAmount
    )
}