package ru.nkyancen.playlistmaker.search.presentation.fragment.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class SearchViewAdapter(
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<SearchViewHolder>() {

    private var tracks = mutableListOf<TrackItem>()

    fun setData(tracks: List<TrackItem>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        return SearchViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: TrackItem)
    }
}