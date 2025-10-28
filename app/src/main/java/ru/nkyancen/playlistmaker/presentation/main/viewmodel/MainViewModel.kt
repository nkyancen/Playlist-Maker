package ru.nkyancen.playlistmaker.presentation.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.nkyancen.playlistmaker.core.utils.SingleLiveEvent
import ru.nkyancen.playlistmaker.presentation.main.model.MainEvent

class MainViewModel : ViewModel() {
    private val transitionLiveData = SingleLiveEvent<MainEvent>()
    fun observeState(): LiveData<MainEvent> = transitionLiveData

    private fun setEvent(state: MainEvent) {
        transitionLiveData.value = state
    }

    fun transitToSearch() {
        setEvent(MainEvent.TransitionToSearch)
    }

    fun transitToMedia() {
        setEvent(MainEvent.TransitionToMediaLibrary)
    }

    fun transitToSettings() {
        setEvent(MainEvent.TransitionToSettings)
    }
}