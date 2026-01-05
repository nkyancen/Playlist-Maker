package ru.nkyancen.playlistmaker.search.data.sources.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ru.nkyancen.playlistmaker.search.data.dto.TrackSearchResponse

interface TunesApi {
    @GET("search?entity=song")
    suspend fun getTracks(
        @Query("term") text: String
    ): TrackSearchResponse
}