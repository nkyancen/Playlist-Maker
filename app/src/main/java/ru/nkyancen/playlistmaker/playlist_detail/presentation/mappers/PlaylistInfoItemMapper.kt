package ru.nkyancen.playlistmaker.playlist_detail.presentation.mappers

import ru.nkyancen.playlistmaker.playlist_detail.domain.model.PlaylistInfo
import ru.nkyancen.playlistmaker.playlist_detail.presentation.model.PlaylistInfoItem

class PlaylistInfoItemMapper {
    fun mapToDomain(dto: PlaylistInfoItem) = PlaylistInfo(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        coverName = dto.coverName
    )
}