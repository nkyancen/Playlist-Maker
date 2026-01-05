package ru.nkyancen.playlistmaker.core.utils

interface LocalPrefsClient<T> {
    fun saveData(data: T)

    fun loadData(default: T): T
}