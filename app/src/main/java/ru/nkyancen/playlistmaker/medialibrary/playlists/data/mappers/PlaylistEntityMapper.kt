package ru.nkyancen.playlistmaker.medialibrary.playlists.data.mappers

import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.entity.PlaylistEntity
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.model.Playlist

class PlaylistEntityMapper: PlaylistMapper<PlaylistEntity> {
    override fun mapFromDomain(model: Playlist) = PlaylistEntity(
        id = model.id,
        title = model.title,
        description = model.description,
        coverImage = model.coverImage,
        listOfTracksId = model.listOfTracks,
        tracksAmount = model.tracksAmount
    )

    override fun mapToDomain(dto: PlaylistEntity) = Playlist(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        coverImage = dto.coverImage,
        listOfTracks = dto.listOfTracksId,
        tracksAmount = dto.tracksAmount
    )
}