package ru.nkyancen.playlistmaker.data.sources.remote

import ru.nkyancen.playlistmaker.data.model.Request
import ru.nkyancen.playlistmaker.data.model.Response

interface RemoteClient {
    fun doRequest(dto: Request): Response
}