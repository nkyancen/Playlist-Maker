package ru.nkyancen.playlistmaker.search.data.sources.remote


import ru.nkyancen.playlistmaker.search.data.dto.Request
import ru.nkyancen.playlistmaker.search.data.dto.Response
import ru.nkyancen.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitClient(private val retrofit: TunesApi) : RemoteClient {

    override suspend fun doRequest(dto: Request): Response =
        retrofit
            .getTracks((dto as TrackSearchRequest).expression)
}
