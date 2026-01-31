package ru.nkyancen.playlistmaker.player.presentation.fragment.playlists

import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

class PlayerPlaylistViewAdapter(
    private val externalInteractor: ExternalInteractor,
    private val clickListener: PlaylistClickListener
) : RecyclerView.Adapter<PlayerPlaylistsViewHolder>() {

    private val playlists = mutableListOf<PlaylistItem>()

    fun setData(playlists: List<PlaylistItem>) {
        this.playlists.clear()
        this.playlists.addAll(playlists)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlayerPlaylistsViewHolder {
        return PlayerPlaylistsViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: PlayerPlaylistsViewHolder,
        position: Int
    ) {
        holder.bind(
            playlists[position],
            externalInteractor.getUriByCoverName(playlists[position].coverImage)
        )

        holder.itemView.setOnClickListener {
            clickListener.onPlaylistClick(playlists[position])
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun interface ExternalInteractor {
        fun getUriByCoverName(coverName: String): Uri?
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: PlaylistItem)
    }
}