package ru.nkyancen.playlistmaker.player.presentation.fragment.playlists

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.PlaylistItemViewBinding
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

class PlayerPlaylistsViewHolder(private val binding: PlaylistItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: PlaylistItem, coverUri: Uri?) {
        val context = binding.root.context

        if (coverUri != null) {
            binding.playlistItemCover.setImageURI(coverUri)
        } else {
            binding.playlistItemCover.setImageResource(R.drawable.ic_placeholder_160)
        }

        binding.apply {
            playlistItemTitle.text = model.title
            playlistItemTracksAmount.text = context.resources.getQuantityString(
                R.plurals.track_amount,
                model.tracksAmount,
                model.tracksAmount
            )
        }
    }

    companion object{
        fun from(parent: ViewGroup): PlayerPlaylistsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistItemViewBinding.inflate(inflater, parent, false)
            return PlayerPlaylistsViewHolder(binding)

        }
    }
}