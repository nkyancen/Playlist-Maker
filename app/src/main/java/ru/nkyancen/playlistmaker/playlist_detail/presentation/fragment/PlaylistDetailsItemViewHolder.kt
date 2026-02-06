package ru.nkyancen.playlistmaker.playlist_detail.presentation.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.databinding.PlaylistDetailsItemViewBinding
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class PlaylistDetailsItemViewHolder(private val binding: PlaylistDetailsItemViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(model: TrackItem) {

        val albumImageUrl = model.albumPoster

        Glide.with(binding.root.context)
            .load(albumImageUrl)
            .placeholder(R.drawable.ic_placeholder_48)
            .error(R.drawable.ic_placeholder_48)
            .fitCenter()
            .transform(
                RoundedCorners(
                    Converter.dpToPx(2.0f, binding.root)
                )
            )
            .into(binding.playlistDetailsItemAlbumImage)

        binding.apply {
            playlistDetailsItemTrackName.text = model.trackName
            playlistDetailsItemArtistName.text = model.artistName
            playlistDetailsItemArtistName.requestLayout()
            playlistDetailsItemTrackTime.text = Converter.formatTime(model.trackTime)
        }
    }

    //
    companion object {
        fun from(parent: ViewGroup): PlaylistDetailsItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = PlaylistDetailsItemViewBinding.inflate(inflater, parent, false)
            return PlaylistDetailsItemViewHolder(binding)
        }
    }
}