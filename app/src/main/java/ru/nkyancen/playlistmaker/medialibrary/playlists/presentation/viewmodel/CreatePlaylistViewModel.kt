package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import ru.nkyancen.playlistmaker.core.utils.PlaylistMapper
import ru.nkyancen.playlistmaker.core.utils.SingleLiveEvent
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistCoverInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.domain.api.PlaylistInteractor
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.CreatePlaylistState
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

class CreatePlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistMapper: PlaylistMapper<PlaylistItem>,
    private val playlistCoverInteractor: PlaylistCoverInteractor
) : ViewModel() {
    private var playlistId: Long = 0L

    fun setPlaylistId(newId: Long) {
        playlistId = newId
    }

    private val createPlaylistStateLiveData =
        MutableLiveData<CreatePlaylistState>(
            CreatePlaylistState.Content(false, null)
        )

    fun observeNewPlaylistState(): LiveData<CreatePlaylistState> = createPlaylistStateLiveData

    private val showMessageLiveData = SingleLiveEvent<String>()
    fun observeShowMessage(): LiveData<String> = showMessageLiveData

    private var coverName: String = ""
    private var title: String = ""
    private var description: String = ""
    private var currentUri: Uri? = null

    fun initializeContent() {
        viewModelScope.launch {
            playlistInteractor
                .getPlaylistById(playlistId)
                .collect {
                    coverName = it.coverImage
                    title = it.title
                    description = it.description
                    currentUri = getUriForCover(it.coverImage)

                    renderState(
                        CreatePlaylistState.Init(
                            it.title,
                            it.description,
                            true,
                            getUriForCover(it.coverImage)
                        )
                    )
                }
        }
    }

    fun getUriForCover(coverName: String): Uri? {
        return if (coverName.isNotEmpty()) {
            playlistCoverInteractor.loadImageFromStorage(coverName)
        } else {
            null
        }
    }


    fun isNewPlaylist(): Boolean = playlistId == 0L

    fun setCreateButtonEnable(newTitle: String) {
        renderState(
            CreatePlaylistState.Content(
                newTitle != "",
                createPlaylistStateLiveData.value?.imageUri
            )
        )
    }

    fun setImageUri(uri: Uri?) {
        renderState(
            CreatePlaylistState.Content(
                createPlaylistStateLiveData.value?.isButtonEnable ?: false,
                uri
            )
        )
    }

    fun isImageSet() = createPlaylistStateLiveData.value?.imageUri != null

    private fun renderState(state: CreatePlaylistState) {
        createPlaylistStateLiveData.postValue(state)
    }


    fun savePlaylist(title: String, description: String?, coroutineExec: CompletedCoroutineExec) {
        val newCoverName = if (isImageSet()) {
            "${System.currentTimeMillis()}.jpg"
        } else {
            ""
        }

        if (newCoverName.isNotEmpty()) {
            saveCoverToStorage(newCoverName)
            if (coverName != "") {
                playlistCoverInteractor.deleteImageFromStorage(coverName)
            }
        }

        if (isNewPlaylist()) {
            showMessageLiveData.setValue(title)
        }

        viewModelScope.launch {
            coroutineExec.afterCompletion(
                if (isNewPlaylist()) {
                    playlistInteractor
                        .savePlaylistToStorage(
                            playlistMapper.mapToDomain(
                                PlaylistItem(
                                    id = 0,
                                    title = title,
                                    description = description ?: "",
                                    coverImage = newCoverName
                                )
                            )
                        ).single()
                } else {
                    playlistInteractor.savePlaylistUpdate(
                        playlistMapper.mapToDomain(
                            PlaylistItem(
                                id = playlistId,
                                title = title,
                                description = description ?: "",
                                coverImage = newCoverName
                            )
                        )
                    ).single()
                }
            )
        }
    }

    fun interface CompletedCoroutineExec {
        fun afterCompletion(result: Any)
    }

    private fun saveCoverToStorage(coverName: String) {
        playlistCoverInteractor.saveImageToStorage(
            createPlaylistStateLiveData.value!!.imageUri!!,
            coverName
        )
    }

}