package ru.nkyancen.playlistmaker.data.sources

interface LocalPrefsClient<T> {
    fun save(data: T)

    fun load(default: T): T
}