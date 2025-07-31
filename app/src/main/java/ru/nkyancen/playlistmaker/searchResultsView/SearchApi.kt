package ru.nkyancen.playlistmaker.searchResultsView

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("/search?entity=song")
    fun getTracks(
        @Query("term") text: String
    ) : Call<TracksResponse>
}