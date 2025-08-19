package ru.nkyancen.playlistmaker.searchResults

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.nkyancen.playlistmaker.model.TracksResponse

interface SearchApi {
    @GET("/search?entity=song")
    fun getTracks(
        @Query("term") text: String
    ) : Call<TracksResponse>
}