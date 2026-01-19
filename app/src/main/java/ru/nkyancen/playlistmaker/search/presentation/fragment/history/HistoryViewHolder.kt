package ru.nkyancen.playlistmaker.search.presentation.fragment.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.databinding.HistoryItemViewBinding
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class HistoryViewHolder(private val binding: HistoryItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

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
            .into(binding.historyItemAlbumImage)

        binding.apply {
            historyItemTrackName.text = model.trackName
            historyItemArtistName.text = model.artistName
            historyItemArtistName.requestLayout()
            historyItemTrackTime.text = Converter.formatTime(model.trackTime)
        }
    }

    companion object {
        fun from(parent: ViewGroup): HistoryViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = HistoryItemViewBinding.inflate(inflater, parent, false)
            return HistoryViewHolder(binding)
        }
    }
}