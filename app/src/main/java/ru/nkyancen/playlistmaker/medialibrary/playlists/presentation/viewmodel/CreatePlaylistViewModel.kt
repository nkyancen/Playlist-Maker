package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.core.utils.SingleLiveEvent
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.ExternalStorageInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.NewPlaylistState
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistMapper: PlaylistMapper<PlaylistItem>,
    private val externalStorageInteractor: ExternalStorageInteractor
) : ViewModel() {

    private val createPlaylistStateLiveData =
        MutableLiveData<NewPlaylistState>(
            NewPlaylistState.Content(
                isButtonEnable = false, imageUri = null
            )
        )

    fun observeNewPlaylistState(): LiveData<NewPlaylistState> = createPlaylistStateLiveData

    private val showMessageLiveData = SingleLiveEvent<String>()
    fun observeShowMessage(): LiveData<String> = showMessageLiveData

    fun setCreateButtonEnable(isEnabled: Boolean) {
        renderState(
            createPlaylistStateLiveData.value!!.apply {
                isButtonEnable = isEnabled
            }
        )
    }

    fun setImageUri(uri: Uri?) {
        renderState(
            createPlaylistStateLiveData.value!!.apply {
                imageUri = uri
            }
        )
    }

    fun isImageSet() = createPlaylistStateLiveData.value!!.imageUri != null

    private fun renderState(state: NewPlaylistState) {
        createPlaylistStateLiveData.postValue(state)
    }


    fun savePlaylist(title: String, description: String?) {
        val coverName = if (isImageSet()) {
            "${System.currentTimeMillis()}.jpg"
        } else {
            ""
        }

        viewModelScope.launch {
            playlistInteractor.savePlaylistToStorage(
                playlistMapper.mapToDomain(
                    PlaylistItem(
                        id = 0,
                        title = title,
                        description = description ?: "",
                        coverImage = coverName
                    )
                )
            )
        }

        if (coverName.isNotEmpty()) {
            saveCoverToStorage(coverName)
        }

        showMessageLiveData.postValue(title)
    }

    private fun saveCoverToStorage(coverName: String){
        externalStorageInteractor.saveImageToStorage(
            createPlaylistStateLiveData.value!!.imageUri!!,
            coverName
        )
    }

}