package ru.nkyancen.playlistmaker.presentation.search.viewmodel

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.nkyancen.playlistmaker.core.creator.Creator
import ru.nkyancen.playlistmaker.domain.search.api.TrackInteractor
import ru.nkyancen.playlistmaker.domain.search.consumer.Consumer
import ru.nkyancen.playlistmaker.domain.search.models.Resource
import ru.nkyancen.playlistmaker.domain.search.models.Track
import ru.nkyancen.playlistmaker.presentation.search.mappers.TrackItemMapper
import ru.nkyancen.playlistmaker.presentation.search.model.TrackItem
import ru.nkyancen.playlistmaker.presentation.search.model.TracksSearchState

class SearchViewModel(private val trackInteractor: TrackInteractor) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val itemMapper = TrackItemMapper()

    private var latestSearchText: String? = null

    private val searchStateLiveData = MutableLiveData<TracksSearchState>(TracksSearchState.Default(false))
    fun observeSearchState(): LiveData<TracksSearchState> = searchStateLiveData


    fun searchDebounce(currentSearchText: String) {
        if (latestSearchText == currentSearchText) {
            return
        }

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable {
            searchRequest(currentSearchText)
        }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    fun searchRequest(searchText: String) {
        if (searchText.isNotEmpty()) {
            this.latestSearchText = searchText
            renderState(TracksSearchState.Loading)

            trackInteractor.searchTracks(
                searchText,
                object : Consumer {
                    override fun consume(tracksResponse: Resource<List<Track>?>) {

                        handler.post {
                            if (tracksResponse.data != null && tracksResponse.expression == searchText) {
                                if (tracksResponse.data.isEmpty()) {
                                    renderState(TracksSearchState.EmptyResponse)
                                } else {
                                    renderState(
                                        TracksSearchState.ShowContent(
                                            itemMapper.mapListToItem(tracksResponse.data)
                                        )
                                    )
                                }
                            } else if (tracksResponse.data == null) {
                                renderState(TracksSearchState.ErrorResponse)
                            } else {
                                handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
                            }
                        }
                    }
                }
            )
        }
    }


    fun clearSearchQuery() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        renderState(
            TracksSearchState.Clear(
                loadHistory()
            )
        )
    }

    private fun loadHistory(): List<TrackItem> = itemMapper.mapListToItem(
        trackInteractor.loadHistoryOfPlayedTracks()
    )

    fun clearHistory() {
        trackInteractor.clearTracksHistory()
        renderState(
            TracksSearchState.Clear(emptyList())
        )
    }

    fun addToHistory(item: TrackItem) {
       trackInteractor.addSelectedTrackToHistory(
            itemMapper.mapToDomain(item)
        )
    }

    private fun renderState(state: TracksSearchState) {
        searchStateLiveData.postValue(state)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2_000L
        val SEARCH_REQUEST_TOKEN = Any()

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val trackInteractor = Creator.provideTrackInteractor()
                SearchViewModel(trackInteractor)
            }
        }
    }
}