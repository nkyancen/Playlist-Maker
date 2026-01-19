package ru.nkyancen.playlistmaker.search.data.impl

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.nkyancen.playlistmaker.core.utils.LocalPrefsClient
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.medialibrary.favorites.data.sources.local.db.AppDatabase
import ru.nkyancen.playlistmaker.search.data.dto.TrackHistory
import ru.nkyancen.playlistmaker.search.domain.api.HistoryRepository
import ru.nkyancen.playlistmaker.search.domain.models.Track

class HistoryRepositoryImpl(
    private val historyPrefsClient: LocalPrefsClient<String>,
    private val historyMapper: TrackMapper<TrackHistory>,
    private val gson: Gson,
    private val appDatabase: AppDatabase
) : HistoryRepository {

    override fun clearTracksHistory() {
        historyPrefsClient.saveData(
            ""
        )
    }

    override fun loadTracksFromHistory(): Flow<List<Track>> = flow {
        val historyString = historyPrefsClient.loadData(
            ""
        )

        val history = try {
            gson.fromJson(historyString, Array<TrackHistory>::class.java).toList()
        } catch (_: Exception) {
            emptyList()
        }

        val tracksId = appDatabase.favoritesDao().getTracksId()

        emit(
            history.map { dto ->
                historyMapper.mapToDomain(dto).apply {
                    isFavorite = dto.id in tracksId
                }
            }
        )

    }

    override fun addSelectedTrackToHistory(newItem: Track) {
        val history = try {
            gson.fromJson(
                historyPrefsClient.loadData(
                    ""
                ),
                Array<TrackHistory>::class.java
            ).toMutableList()
        } catch (_: Exception) {
            mutableListOf()
        }

        var deletedIndex = -1

        history.forEachIndexed { index, item ->
            if (item.id == newItem.id) {
                deletedIndex = index
            }
        }

        if (deletedIndex > 0) {
            history.removeAt(deletedIndex)
        }

        if (history.size == HISTORY_LIMIT) {
            history.removeAt(history.lastIndex)
        }

        if (deletedIndex != 0) {
            history.add(0, historyMapper.mapFromDomain(newItem))
        }

        historyPrefsClient.saveData(
            gson.toJson(
                history.toList()
            )
        )
    }

    companion object {
        const val HISTORY_LIMIT = 10
    }
}