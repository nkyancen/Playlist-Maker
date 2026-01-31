package ru.nkyancen.playlistmaker.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.mappers.TrackEntityMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.entity.PlaylistEntity
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.mappers.PlaylistEntityMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.mappers.PlaylistItemMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem
import ru.nkyancen.playlistmaker.search.data.dto.TrackData
import ru.nkyancen.playlistmaker.search.data.dto.TrackHistory
import ru.nkyancen.playlistmaker.search.data.mappers.TrackDataMapper
import ru.nkyancen.playlistmaker.search.data.mappers.TrackHistoryMapper
import ru.nkyancen.playlistmaker.search.presentation.mappers.TrackItemMapper
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

const val HISTORY_MAPPER = "historyMapper"
const val SEARCH_MAPPER = "searchMapper"
const val ENTITY_MAPPER = "entityMapper"
const val ITEM_MAPPER = "itemMapper"
const val PLAYLIST_ENTITY_MAPPER = "playlistEntityMapper"
const val PLAYLIST_ITEM_MAPPER = "playlistItemMapper"

val mapperModule = module {

    single<TrackMapper<TrackHistory>>(named(HISTORY_MAPPER)) {
        TrackHistoryMapper()
    }

    single<TrackMapper<TrackData>>(named(SEARCH_MAPPER)) {
        TrackDataMapper()
    }

    single<TrackMapper<TrackEntity>>(named(ENTITY_MAPPER)) {
        TrackEntityMapper()
    }

    single<TrackMapper<TrackItem>>(named(ITEM_MAPPER)) {
        TrackItemMapper()
    }

    single<PlaylistMapper<PlaylistEntity>>(named(PLAYLIST_ENTITY_MAPPER)) {
        PlaylistEntityMapper()
    }

    single<PlaylistMapper<PlaylistItem>>(named(PLAYLIST_ITEM_MAPPER)) {
        PlaylistItemMapper()
    }
}