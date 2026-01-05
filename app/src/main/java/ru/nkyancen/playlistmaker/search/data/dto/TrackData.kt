package ru.nkyancen.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

data class TrackData(
    @SerializedName("trackId") val id: Long,
    @SerializedName("trackName") val trackName: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTime: Long?,
    @SerializedName("artworkUrl100") val albumPosterUrl: String?,
    @SerializedName("collectionName") val albumName: String?,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("primaryGenreName") val genre: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("previewUrl") val previewUrl: String?
) {

    fun getTrackYear() = releaseDate?.split('-')[0] ?: ""
}

