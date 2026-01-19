package ru.nkyancen.playlistmaker.medialibrary.favorites.presentation.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.databinding.FavoritesItemViewBinding
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class FavoritesViewHolder(private val binding: FavoritesItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

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
            .into(binding.favoritesItemAlbumImage)

        binding.apply {
            favoritesItemTrackName.text = model.trackName
            favoritesItemArtistName.text = model.artistName
            favoritesItemArtistName.requestLayout()
            favoritesItemTrackTime.text = Converter.formatTime(model.trackTime)
        }
    }

    companion object {
        fun from(parent: ViewGroup): FavoritesViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = FavoritesItemViewBinding.inflate(inflater, parent, false)
            return FavoritesViewHolder(binding)
        }
    }
}