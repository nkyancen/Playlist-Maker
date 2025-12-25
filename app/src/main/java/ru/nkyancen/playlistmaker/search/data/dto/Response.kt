package ru.nkyancen.playlistmaker.search.data.dto

open class Response {
    var resultCode = 0

    companion object {
        const val SUCCESS = 200
        const val ERROR = 400
    }

    fun isSuccess() = (resultCode == SUCCESS)
}