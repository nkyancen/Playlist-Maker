package ru.nkyancen.playlistmaker.searchResultsView

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.nkyancen.playlistmaker.R

class SearchViewHolder(searchItemView: View) : RecyclerView.ViewHolder(searchItemView) {
    private val searchView: LinearLayout = searchItemView.findViewById(R.id.searchView)
    private val albumImage: ImageView = searchItemView.findViewById(R.id.albumImage)
    private val trackName: TextView = searchItemView.findViewById(R.id.trackName)
    private val artistName: TextView = searchItemView.findViewById(R.id.artistName)
    private val trackTime: TextView = searchItemView.findViewById(R.id.trackTime)

    fun bind(model: Track) {
        val albumImageUrl = model.artworkUrl100

        Glide.with(searchView.context)
            .load(albumImageUrl)
            .placeholder(R.drawable.ic_placeholder_48)
            .fitCenter()
            .transform(RoundedCorners(2))
            .into(albumImage)

        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
    }

}