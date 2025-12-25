package ru.nkyancen.playlistmaker.search.presentation.model

sealed interface TracksSearchState {

    data class ShowContent(
        val tracks: List<TrackItem>
    ) : TracksSearchState

    data class Clear(
        val history: List<TrackItem>
    ): TracksSearchState

    object Loading : TracksSearchState

    object ErrorResponse : TracksSearchState

    object EmptyResponse : TracksSearchState

    data class Default(
        val hasFocus: Boolean
    ): TracksSearchState
}