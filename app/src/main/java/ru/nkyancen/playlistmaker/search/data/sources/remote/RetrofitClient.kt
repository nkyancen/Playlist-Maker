package ru.nkyancen.playlistmaker.search.data.sources.remote

import ru.nkyancen.playlistmaker.search.data.dto.Request
import ru.nkyancen.playlistmaker.search.data.dto.Response
import ru.nkyancen.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitClient (private val retrofit: TunesApi) : RemoteClient {

    override fun doRequest(dto: Request) = try {
        val response = retrofit.getTracks((dto as TrackSearchRequest).expression).execute()
        val body = response.body() ?: Response()

        body.apply { resultCode = response.code() }

    } catch (_: Exception) {
        Response().apply { resultCode = Response.ERROR }
    }
}