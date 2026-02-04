package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.fragment

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

class PlaylistViewAdapter(
    private val playlistItemCoverInteractor: PlaylistItemCoverInteractor,
    private val clickListener: PlaylistClickListener
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
            playlistItemCoverInteractor.getUriByCoverName(playlists[position].coverImage)
        )

        holder.itemView.setOnClickListener {
            clickListener.onClickListener(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun interface PlaylistItemCoverInteractor {
        fun getUriByCoverName(coverName: String): Uri?
    }

    fun interface PlaylistClickListener {
        fun onClickListener(playlistItem: PlaylistItem)
    }
}