package ru.nkyancen.playlistmaker.data.search.sources.remote

import ru.nkyancen.playlistmaker.data.search.dto.Request
import ru.nkyancen.playlistmaker.data.search.dto.Response

interface RemoteClient {
    fun doRequest(dto: Request): Response
}