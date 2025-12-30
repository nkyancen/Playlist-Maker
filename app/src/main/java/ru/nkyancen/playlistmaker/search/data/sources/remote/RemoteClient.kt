package ru.nkyancen.playlistmaker.search.data.sources.remote

import ru.nkyancen.playlistmaker.search.data.dto.Request
import ru.nkyancen.playlistmaker.search.data.dto.Response

interface RemoteClient {
    suspend fun doRequest(dto: Request): Response
}