package ru.nkyancen.playlistmaker.searchResultsView

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("trackName") val trackName: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTime: Long?,
    @SerializedName("artworkUrl100") val albumPosterUrl: String?
)

data class TracksResponse(
    @SerializedName("results") val tracks: List<Track>
)