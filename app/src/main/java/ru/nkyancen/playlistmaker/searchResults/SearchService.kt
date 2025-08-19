package ru.nkyancen.playlistmaker.searchResults

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchService {

    private val searchBaseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(searchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(SearchApi::class.java)
}