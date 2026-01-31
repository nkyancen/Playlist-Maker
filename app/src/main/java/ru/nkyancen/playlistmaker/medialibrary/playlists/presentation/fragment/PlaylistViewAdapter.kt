package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.fragment

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

class PlaylistViewAdapter(
    private val externalInteractor: ExternalInteractor
) : RecyclerView.Adapter<PlaylistsViewHolder>() {

    private val playlists = mutableListOf<PlaylistItem>()

    fun setData(playlists: List<PlaylistItem>) {
        this.playlists.clear()
        this.playlists.addAll(playlists)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistsViewHolder {
        return PlaylistsViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: PlaylistsViewHolder,
        position: Int
    ) {
        holder.bind(
            playlists[position],
            externalInteractor.getUriByCoverName(playlists[position].coverImage)
        )
    }

    override fun getItemCount(): Int = playlists.size

    fun interface ExternalInteractor {
        fun getUriByCoverName(coverName: String): Uri?
    }
}