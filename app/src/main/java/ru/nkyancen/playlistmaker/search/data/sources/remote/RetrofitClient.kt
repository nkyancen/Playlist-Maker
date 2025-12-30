package ru.nkyancen.playlistmaker.search.data.sources.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.nkyancen.playlistmaker.search.data.dto.Request
import ru.nkyancen.playlistmaker.search.data.dto.Response
import ru.nkyancen.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitClient (private val retrofit: TunesApi) : RemoteClient {

    override suspend fun doRequest(dto: Request): Response = withContext(Dispatchers.IO) {
        try {
            val response = retrofit.getTracks((dto as TrackSearchRequest).expression)

            response.apply { resultCode = 200 }

        } catch (_: Exception) {
            Response().apply { resultCode = Response.ERROR }
        }
    }
}