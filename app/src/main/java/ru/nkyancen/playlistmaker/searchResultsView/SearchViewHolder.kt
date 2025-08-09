package ru.nkyancen.playlistmaker.searchResultsView

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.utils.UnitsConverter
import java.text.SimpleDateFormat
import java.util.Locale

class SearchViewHolder(searchItemView: View) : RecyclerView.ViewHolder(searchItemView),
    UnitsConverter {
    private val searchView: LinearLayout = searchItemView.findViewById(R.id.searchItemView)
    private val albumImageView: ImageView = searchItemView.findViewById(R.id.albumImage)
    private val trackNameView: TextView = searchItemView.findViewById(R.id.trackName)
    private val artistNameView: TextView = searchItemView.findViewById(R.id.artistName)
    private val trackTimeView: TextView = searchItemView.findViewById(R.id.trackTime)

    fun bind(model: Track) {

        val albumImageUrl = model.albumPosterUrl

        Glide.with(searchView.context)
            .load(albumImageUrl)
            .placeholder(R.drawable.ic_placeholder_48)
            .fitCenter()
            .transform(
                RoundedCorners(
                    dpToPx(2.0f, searchView)
                )
            )
            .into(albumImageView)

        trackNameView.text = model.trackName ?: ""
        artistNameView.text = model.artistName ?: ""
        artistNameView.requestLayout()
        trackTimeView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime ?: 0L)
    }
}