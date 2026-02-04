package ru.nkyancen.playlistmaker.playlist_detail.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistCoverInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistInteractor
import ru.nkyancen.playlistmaker.playlist_detail.domain.api.PlaylistEditorInteractor
import ru.nkyancen.playlistmaker.playlist_detail.presentation.mappers.PlaylistInfoItemMapper
import ru.nkyancen.playlistmaker.playlist_detail.presentation.model.PlaylistEditorState
import ru.nkyancen.playlistmaker.playlist_detail.presentation.model.PlaylistInfoItem

class PlaylistEditorViewModel(
    private val playlistId: Long,
    private val playlistInfoItemMapper: PlaylistInfoItemMapper,
    private val playlistInteractor: PlaylistInteractor,
    private val playlistCoverInteractor: PlaylistCoverInteractor,
    private val editorInteractor: PlaylistEditorInteractor
) : ViewModel() {

    private val editorStateLiveData =
        MutableLiveData<PlaylistEditorState>(PlaylistEditorState.Content(false, null))

    fun observeEditorState(): LiveData<PlaylistEditorState> = editorStateLiveData

    private var currentCoverName: String = ""
    private var currentTitle: String = ""
    private var currentDescription: String = ""
    private var currentUri: Uri? = null

    init {
        runBlocking {
            playlistInteractor
                .getPlaylistById(playlistId)
                .collect {
                    currentCoverName = it.coverImage
                    currentTitle = it.title
                    currentDescription = it.description
                    currentUri = getUriForCover(currentCoverName)
                }
        }
        renderState(
            PlaylistEditorState.Content(
                true,
                getUriForCover(currentCoverName)
            )
        )
    }

    fun getCurrentTitle() = currentTitle

    fun getCurrentDescription() = currentDescription

    fun setButtonEnable(newTitle: String) {
        renderState(
            PlaylistEditorState.Content(
                isButtonEnable = newTitle != "",
                uri = editorStateLiveData.value!!.uri
            )
        )
    }

    fun setImageUri(uri: Uri?) {
        renderState(
            PlaylistEditorState.Content(
                editorStateLiveData.value!!.isButtonEnable,
                uri
            )
        )
    }

    private fun isImageSet() = editorStateLiveData.value!!.uri != null

    fun saveChanges(title: String, description: String?) {
        val coverName = if (isImageSet()) {
            "${System.currentTimeMillis()}.jpg"
        } else {
            ""
        }

        if (coverName.isNotEmpty()) {
            playlistCoverInteractor.saveImageToStorage(
                imageUri = editorStateLiveData.value!!.uri!!,
                imageTitle = coverName
            )
            playlistCoverInteractor.deleteImageFromStorage(currentCoverName)
        }

        viewModelScope.launch {
            editorInteractor.savePlaylistUpdate(
                playlistInfoItemMapper.mapToDomain(
                    PlaylistInfoItem(
                        playlistId,
                        title,
                        description ?: "",
                        coverName
                    )
                )
            )
        }


    }

    fun getUriForCover(coverName: String): Uri? {
        return if (coverName.isNotEmpty()) {
            playlistCoverInteractor.loadImageFromStorage(coverName)
        } else {
            null
        }
    }

    private fun renderState(state: PlaylistEditorState) {
        editorStateLiveData.postValue(state)
    }
}