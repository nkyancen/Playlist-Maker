package ru.nkyancen.playlistmaker.data.search.sources.local.prefs

interface LocalPrefsClient<T> {
    fun saveData(data: T)

    fun loadData(default: T): T
}