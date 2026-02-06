package ru.nkyancen.playlistmaker.playlist_detail.presentation.fragment

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class PlaylistDetailsItemViewAdapter(
    private val clickListener: TrackClickListener,
    private val longClickListener: TrackLongClickListener
) : RecyclerView.Adapter<PlaylistDetailsItemViewHolder>() {

    private val tracks = mutableListOf<TrackItem>()

    fun setData(tracks: List<TrackItem>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistDetailsItemViewHolder {
        return PlaylistDetailsItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: PlaylistDetailsItemViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            clickListener.onTrackClick(tracks[position])
        }

        holder.itemView.setOnLongClickListener {
            longClickListener.onTrackLongClick(tracks[position])
            true
        }


    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface TrackClickListener {
        fun onTrackClick(track: TrackItem)
    }

    fun interface TrackLongClickListener {
        fun onTrackLongClick(track: TrackItem)
    }
}