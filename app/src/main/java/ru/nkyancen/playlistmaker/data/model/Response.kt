package ru.nkyancen.playlistmaker.data.model

open class Response {
    var resultCode = 0

    companion object {
        const val SUCCESS = 200
        const val ERROR = 400
    }

    fun isSuccess() = (resultCode in SUCCESS.rangeUntil(SUCCESS + 100))
}