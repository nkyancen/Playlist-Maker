package ru.nkyancen.playlistmaker.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import ru.nkyancen.playlistmaker.core.utils.TrackMapper
import ru.nkyancen.playlistmaker.core.utils.debounce
import ru.nkyancen.playlistmaker.search.domain.api.TrackInteractor
import ru.nkyancen.playlistmaker.search.domain.models.Resource
import ru.nkyancen.playlistmaker.search.domain.models.Track
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem
import ru.nkyancen.playlistmaker.search.presentation.model.TracksSearchState

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val itemMapper: TrackMapper<TrackItem>
) : ViewModel() {

    private var latestSearchText: String? = null

    private val movieSearchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { changedText ->
        searchRequest(changedText)
    }

    private val searchStateLiveData =
        MutableLiveData<TracksSearchState>(TracksSearchState.Default(false))

    fun observeSearchState(): LiveData<TracksSearchState> = searchStateLiveData


    fun searchDebounce(currentSearchText: String) {
        if (latestSearchText == currentSearchText) {
            return
        }
        if (currentSearchText.isNotEmpty()) {
            this.latestSearchText = currentSearchText
            movieSearchDebounce(currentSearchText)
        }
    }

    fun searchRequest(searchText: String) {
        renderState(TracksSearchState.Loading)

        val handler = CoroutineExceptionHandler { _, _ ->
            renderState(TracksSearchState.ErrorResponse)
        }

        viewModelScope.launch(handler) {
            trackInteractor
                .searchTracks(searchText)
                .collect { response ->
                    processResult(response)
                }
        }
    }

    private fun processResult(
        response: Resource<List<Track>>
    ) {
        if (response.expression == this.latestSearchText) {
            if (response.data.isEmpty()) {
                renderState(TracksSearchState.EmptyResponse)
            } else {
                renderState(
                    TracksSearchState.ShowContent(
                        itemMapper.mapListFromDomain(response.data)
                            .filter { it.preview.isNotEmpty() }
                    )
                )
            }
        }
    }


    fun clearSearchQuery() {
        renderState(
            TracksSearchState.Clear(
                loadHistory()
            )
        )
    }

    fun loadHistory(): List<TrackItem> = itemMapper.mapListFromDomain(
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2_000L
    }
}