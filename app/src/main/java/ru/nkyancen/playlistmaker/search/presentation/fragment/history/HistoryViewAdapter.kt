package ru.nkyancen.playlistmaker.search.presentation.fragment.history

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class HistoryViewAdapter(
    private val clickListener: TrackClickListener
) : RecyclerView.Adapter<HistoryViewHolder>() {

    private val tracks = mutableListOf<TrackItem>()

    fun setData(tracks: List<TrackItem>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {
        return HistoryViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: HistoryViewHolder,
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