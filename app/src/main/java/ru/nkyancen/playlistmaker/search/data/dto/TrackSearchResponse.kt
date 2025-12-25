package ru.nkyancen.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

data class TrackSearchResponse(
    val expression: String,
    @SerializedName("results") val tracks: List<TrackData>
) : Response()