package ru.nkyancen.playlistmaker.data.search.impl

import com.google.gson.Gson
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.data.LocalPrefsClient
import ru.nkyancen.playlistmaker.data.search.dto.TrackHistory
import ru.nkyancen.playlistmaker.domain.search.api.HistoryRepository
import ru.nkyancen.playlistmaker.domain.search.models.Track

class HistoryRepositoryImpl(
    private val historyPrefsClient: LocalPrefsClient<String>,
    private val historyMapper: TrackMapper<TrackHistory>,
    private val gson: Gson
) : HistoryRepository {

    override fun clearTracksHistory() {
        historyPrefsClient.saveData(
            ""
        )
    }

    override fun loadTracksFromHistory(): List<Track> {
        val history = historyPrefsClient.loadData(
            ""
        )

        return historyMapper.mapListToDomain(
            try {
                gson.fromJson(history, Array<TrackHistory>::class.java).toList()
            } catch (_: Exception) {
                emptyList()
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