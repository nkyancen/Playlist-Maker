package ru.nkyancen.playlistmaker.data

interface LocalPrefsClient<T> {
    fun saveData(data: T)

    fun loadData(default: T): T
}