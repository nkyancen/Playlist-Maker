package ru.nkyancen.playlistmaker.data.sources.local.prefs

interface LocalPrefsClient<T> {
    fun save(data: T)

    fun load(default: T): T
}