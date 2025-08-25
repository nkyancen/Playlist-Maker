package ru.nkyancen.playlistmaker.generalViews

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.model.CURRENT_TRACK_TAG
import ru.nkyancen.playlistmaker.model.Track
import ru.nkyancen.playlistmaker.utils.Converter

class MediaPlayerActivity() : AppCompatActivity(), Converter {

    private lateinit var playerMain: ConstraintLayout
    private lateinit var backButton: MaterialToolbar
    private lateinit var albumImage: ImageView

    private lateinit var titleText: TextView
    private lateinit var artistNameText: TextView

    private lateinit var playButton: ImageButton
    private lateinit var addToPlayListButton: ImageButton
    private lateinit var addToFavoriteButton: ImageButton
    private lateinit var progressText: TextView

    private lateinit var trackTimeText: TextView
    private lateinit var trackAlbumText: TextView
    private lateinit var trackAlbumGroup: Group
    private lateinit var trackYearText: TextView
    private lateinit var trackYearGroup: Group
    private lateinit var trackGenreText: TextView
    private lateinit var trackCountryText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        initializeViews()

        setClickListeners()

        setContentToViews()
    }

    private fun initializeViews() {
        backButton = findViewById(R.id.playerHeader)

        playerMain = findViewById(R.id.playerMain)

        albumImage = findViewById(R.id.playerAlbumImage)
        titleText = findViewById(R.id.playerTitleText)
        artistNameText = findViewById(R.id.playerArtistText)

        playButton = findViewById(R.id.playerPlayButton)
        addToPlayListButton = findViewById(R.id.playerAddToPlayList)
        addToFavoriteButton = findViewById(R.id.playerAddToFavorites)
        progressText = findViewById(R.id.playerProgressTimeText)

        trackTimeText = findViewById(R.id.playerTrackTimeText)
        trackAlbumText = findViewById(R.id.playerTrackAlbumText)
        trackAlbumGroup = findViewById(R.id.playerTrackAlbumGroup)
        trackYearText = findViewById(R.id.playerTrackYearText)
        trackYearGroup = findViewById(R.id.playerTrackYearGroup)
        trackGenreText = findViewById(R.id.playerTrackGenreText)
        trackCountryText = findViewById(R.id.playerTrackCountryText)
    }

    private fun setClickListeners() {
        backButton.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setContentToViews() {
        val currentTrack = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CURRENT_TRACK_TAG, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(CURRENT_TRACK_TAG)
        })!!


        Glide.with(playerMain.context)
            .load(
                currentTrack.getPlayerAlbumImage()
            )
            .placeholder(R.drawable.ic_placeholder_312)
            .fitCenter()
            .transform(
                RoundedCorners(
                    dpToPx(8.0f, playerMain)
                )
            )
            .into(albumImage)

        titleText.text = currentTrack.trackName
        artistNameText.text = currentTrack.artistName

        progressText.text = formatTime(0L)

        trackTimeText.text = formatTime(currentTrack.trackTime)

        if (currentTrack.albumName.isNullOrEmpty()) {
            trackAlbumGroup.visibility = View.GONE
        } else {
            trackAlbumGroup.visibility = View.VISIBLE
            trackAlbumText.text = currentTrack.albumName
        }

        if (currentTrack.releaseDate.isNullOrEmpty()) {
            trackYearGroup.visibility = View.GONE
        } else {
            trackYearGroup.visibility = View.VISIBLE
            trackYearText.text = currentTrack.getTrackYear()
        }

        trackGenreText.text = currentTrack.genre ?: ""

        trackCountryText.text = currentTrack.country ?: ""
    }
}