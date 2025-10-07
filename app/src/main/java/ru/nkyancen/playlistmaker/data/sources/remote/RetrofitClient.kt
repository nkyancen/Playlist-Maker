package ru.nkyancen.playlistmaker.data.sources.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nkyancen.playlistmaker.data.model.Request
import ru.nkyancen.playlistmaker.data.model.Response
import ru.nkyancen.playlistmaker.data.model.TrackSearchRequest
import ru.nkyancen.playlistmaker.data.sources.RemoteClient

object RetrofitClient : RemoteClient {
    private const val SEARCH_BASE_URL = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(SEARCH_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(TunesApi::class.java)

    override fun doRequest(dto: Request) = try {
        val response = api.getTracks((dto as TrackSearchRequest).expression).execute()
        val body = response.body() ?: Response()

        body.apply { resultCode = response.code() }

    } catch (_: Exception) {
        Response().apply { resultCode = Response.ERROR }
    }
}