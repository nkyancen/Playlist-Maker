package ru.nkyancen.playlistmaker.searchResultsView

import android.content.SharedPreferences
import com.google.gson.Gson
import androidx.core.content.edit

const val SEARCH_HISTORY_TAG = "Search History"

class SearchHistory(
    val sharedPrefs: SharedPreferences
) {

    fun write(historyList: List<Track>) {
        val history = Gson().toJson(historyList)
        sharedPrefs.edit {
            putString(SEARCH_HISTORY_TAG, history)
        }
    }

    fun read(): ArrayList<Track> {
        val history = sharedPrefs.getString(
            SEARCH_HISTORY_TAG,
            null
        ) ?: return ArrayList(emptyList<Track>())

        return ArrayList(Gson().fromJson(history, Array<Track>::class.java).toList())
    }

    fun add(newItem: Track) {
        val history = read()

        var deletedIndex = 0

        history.forEachIndexed { index, item ->
            if (item.id == newItem.id) {
                deletedIndex = index
            }
        }

        if (deletedIndex > 0) {
            history.removeAt(deletedIndex)
        }

        if (history.size == HISTORY_LIMIT) {
            history.removeAt(HISTORY_LIMIT - 1)
        }

        history.add(0, newItem)

        write(history)
    }

    companion object {
        const val HISTORY_LIMIT = 10
    }
}