package ru.nkyancen.playlistmaker.data.repository

import com.google.gson.Gson
import ru.nkyancen.playlistmaker.data.mappers.TrackHistoryMapper
import ru.nkyancen.playlistmaker.data.model.TrackHistory
import ru.nkyancen.playlistmaker.data.sources.LocalPrefsClient
import ru.nkyancen.playlistmaker.domain.models.Track
import ru.nkyancen.playlistmaker.domain.repository.track.HistoryRepository

class HistoryRepositoryImpl(
    private val prefsClient: LocalPrefsClient<String>,
    private val gson: Gson
) : HistoryRepository {


    override fun saveTracksToHistory(tracks: List<Track>) {
        val history = gson.toJson(
            TrackHistoryMapper().mapListToHistory(tracks)
        )
        prefsClient.save(
            history
        )
    }

    override fun loadTracksFromHistory(): List<Track> {
        val history = prefsClient.load(
            ""
        )

        return TrackHistoryMapper().mapListToDomain(
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
                prefsClient.load(
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
            history.add(0, TrackHistoryMapper().mapToHistory(newItem))
        }

        prefsClient.save(
            gson.toJson(
                history.toList()
            )
        )
    }

    companion object {
        const val HISTORY_LIMIT = 10
    }
}

