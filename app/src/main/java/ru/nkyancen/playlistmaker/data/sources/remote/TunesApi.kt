package ru.nkyancen.playlistmaker.data.sources.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.nkyancen.playlistmaker.data.model.TrackSearchResponse

interface TunesApi {
    @GET("search?entity=song")
    fun getTracks(
        @Query("term") text: String
    ): Call<TrackSearchResponse>
}