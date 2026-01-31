package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.fragment

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.PlaylistItemViewForGridBinding
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem

class PlaylistsViewHolder(
    private val binding: PlaylistItemViewForGridBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: PlaylistItem, coverUri: Uri?) {

        if (coverUri != null) {
            binding.playlistItemGridCover.setImageURI(coverUri)
        } else {
            binding.playlistItemGridCover.setImageResource(R.drawable.ic_placeholder_160)
        }

        binding.apply {
            playlistItemGridTitle.text = model.title
            playlistItemGridTracksAmount.text = binding.root.context.resources.getQuantityString(
                R.plurals.track_amount,
                model.tracksAmount,
                model.tracksAmount
            )
        }
    }

    companion object {
        fun from(parent: ViewGroup): PlaylistsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistItemViewForGridBinding.inflate(inflater, parent, false)
            return PlaylistsViewHolder(binding)

        }
    }
}