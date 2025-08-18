package ru.nkyancen.playlistmaker.searchResultsView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.generalView.MediaPlayerActivity


class SearchViewAdapter(
    private val tracks: List<Track>,
    private val historyUser: SearchHistory
) : RecyclerView.Adapter<SearchViewHolder> () {

    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context)
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

            val targetIntent = Intent(context, MediaPlayerActivity::class.java)
            context.startActivity(targetIntent)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}