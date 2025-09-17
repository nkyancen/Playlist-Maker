package ru.nkyancen.playlistmaker.searchResults

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.generalViews.MediaPlayerActivity
import ru.nkyancen.playlistmaker.model.*


class SearchViewAdapter(
    private val tracks: List<Track>,
    private val historyUser: SearchHistory
) : RecyclerView.Adapter<SearchViewHolder>() {

    private lateinit var context: Context

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())

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
            if (clickDebounce()) {
                val targetIntent = Intent(context, MediaPlayerActivity::class.java)
                targetIntent.putExtra(CURRENT_TRACK_TAG, tracks[position])

                historyUser.add(tracks[position])

                context.startActivity(targetIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }

        return current
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}