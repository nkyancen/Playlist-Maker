package ru.nkyancen.playlistmaker.di

import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.entity.FavoriteTrackEntity
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.mappers.FavoriteTrackEntityMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.entity.PlaylistEntity
import ru.nkyancen.playlistmaker.medialibrary.playlists.data.mappers.PlaylistEntityMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.mappers.PlaylistItemMapper
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem
import ru.nkyancen.playlistmaker.playlist_detail.data.entity.TrackEntity
import ru.nkyancen.playlistmaker.playlist_detail.data.mappers.TrackEntityMapper
import ru.nkyancen.playlistmaker.search.data.dto.TrackData
import ru.nkyancen.playlistmaker.search.data.dto.TrackHistory
import ru.nkyancen.playlistmaker.search.data.mappers.TrackDataMapper
import ru.nkyancen.playlistmaker.search.data.mappers.TrackHistoryMapper
import ru.nkyancen.playlistmaker.search.presentation.mappers.TrackItemMapper
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

const val HISTORY_MAPPER = "historyMapper"
const val SEARCH_MAPPER = "searchMapper"

const val FAVORITES_ENTITY_MAPPER = "favoritesEntityMapper"
const val TRACK_ENTITY_MAPPER = "entityMapper"
const val PLAYLIST_ENTITY_MAPPER = "playlistEntityMapper"

const val ITEM_MAPPER = "itemMapper"
const val PLAYLIST_ITEM_MAPPER = "playlistItemMapper"

val mapperModule = module {

    single<TrackMapper<TrackHistory>>(named(HISTORY_MAPPER)) {
        TrackHistoryMapper()
    }

    single<TrackMapper<TrackData>>(named(SEARCH_MAPPER)) {
        TrackDataMapper()
    }

    single<TrackMapper<FavoriteTrackEntity>>(named(FAVORITES_ENTITY_MAPPER)) {
        FavoriteTrackEntityMapper()
    }

    single<TrackMapper<TrackItem>>(named(ITEM_MAPPER)) {
        TrackItemMapper()
    }

    single<TrackMapper<TrackEntity>>(named(TRACK_ENTITY_MAPPER)) {
        TrackEntityMapper()
    }

    single<PlaylistMapper<PlaylistEntity>>(named(PLAYLIST_ENTITY_MAPPER)) {
        PlaylistEntityMapper()
    }

    single<PlaylistMapper<PlaylistItem>>(named(PLAYLIST_ITEM_MAPPER)) {
        PlaylistItemMapper()
    }
}