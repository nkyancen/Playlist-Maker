package ru.nkyancen.playlistmaker.searchResultsView

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("trackId") val id: Long,
    @SerializedName("trackName") val trackName: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTime: Long?,
    @SerializedName("artworkUrl100") val albumPosterUrl: String?,
    @SerializedName("collectionName") val albumName: String?,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("primaryGenreName") val genre: String?,
    @SerializedName("country") val country: String?
) {
    fun getPlayerAlbumImage() = albumPosterUrl?.replaceAfterLast('/',"512x512bb.jpg")

    fun getTrackYear() = releaseDate?.split('-')[0]
}

data class TracksResponse(
    @SerializedName("results") val tracks: List<Track>
)