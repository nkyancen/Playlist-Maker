package ru.nkyancen.playlistmaker.search.presentation.fragment.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.databinding.SearchItemViewBinding
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class SearchViewHolder(private val binding: SearchItemViewBinding) :
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
            .into(binding.searchItemAlbumImage)

        binding.apply {
            searchItemTrackName.text = model.trackName
            searchItemArtistName.text = model.artistName
            searchItemArtistName.requestLayout()
            searchItemTrackTime.text = Converter.formatTime(model.trackTime)
        }
    }

    companion object {
        fun from(parent: ViewGroup): SearchViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SearchItemViewBinding.inflate(inflater, parent, false)
            return SearchViewHolder(binding)
        }
    }
}