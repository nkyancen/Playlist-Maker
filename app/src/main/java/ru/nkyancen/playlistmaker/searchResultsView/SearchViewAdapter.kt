package ru.nkyancen.playlistmaker.searchResultsView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.R

class SearchViewAdapter(
    private val tracks: List<Track>,
    private val historyUser: SearchHistory
) : RecyclerView.Adapter<SearchViewHolder> () {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.search_item_view,
                parent,
                false
            )
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: SearchViewHolder,
        position: Int
    ) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            historyUser.add(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}