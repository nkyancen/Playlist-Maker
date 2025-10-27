package ru.nkyancen.playlistmaker.presentation.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.nkyancen.playlistmaker.core.utils.SingleLiveEvent
import ru.nkyancen.playlistmaker.presentation.main.model.MainState

class MainViewModel : ViewModel() {
    private val transitionLiveData = SingleLiveEvent<MainState>()
    fun observeState(): LiveData<MainState> = transitionLiveData

    fun setState(state: MainState) {
        transitionLiveData.value = state
    }
}