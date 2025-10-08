package ru.nkyancen.playlistmaker.data.sources.remote

import retrofit2.Retrofit
import ru.nkyancen.playlistmaker.data.model.Request
import ru.nkyancen.playlistmaker.data.model.Response
import ru.nkyancen.playlistmaker.data.model.TrackSearchRequest

class RetrofitClient (private val retrofit: Retrofit) : RemoteClient {

    private fun getApi() = retrofit.create(TunesApi::class.java)

    override fun doRequest(dto: Request) = try {
        val response = getApi().getTracks((dto as TrackSearchRequest).expression).execute()
        val body = response.body() ?: Response()

        body.apply { resultCode = response.code() }

    } catch (_: Exception) {
        Response().apply { resultCode = Response.ERROR }
    }
}