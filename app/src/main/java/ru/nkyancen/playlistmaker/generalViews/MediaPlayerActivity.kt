package ru.nkyancen.playlistmaker.generalViews

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import ru.nkyancen.playlistmaker.model.*
import ru.nkyancen.playlistmaker.common.MediaPlayer
import ru.nkyancen.playlistmaker.common.Converter


class MediaPlayerActivity : AppCompatActivity() {

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

    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        initializeViews()

        setContentToViews()

        setClickListeners()
    }

    override fun onPause() {
        super.onPause()
        MediaPlayer.pausePlayer()
    }

    override fun onRestart() {
        super.onRestart()
        playButtonControl()
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayer.releasePlayer()
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

        playButton.setOnClickListener {
            MediaPlayer.playbackControl()

            playButtonControl()
            timerControl()
        }
    }

    private fun playButtonControl() {
        if (MediaPlayer.playerState == MediaPlayer.State.PLAYING) {
            playButton.setImageResource(R.drawable.player_pause_button_100)
        } else {
            playButton.setImageResource(R.drawable.player_play_button_100)
        }
    }

    private fun timerControl() {
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    if (MediaPlayer.playerState == MediaPlayer.State.PLAYING) {
                        progressText.text = Converter.formatTime(
                            MediaPlayer.getCurrentPosition().toLong()
                        )
                        
                        handler.postDelayed(
                            this,
                            TIMER_UPDATE_DELAY
                        )
                    } else {
                        handler.removeCallbacks(this)

                        if (MediaPlayer.playerState == MediaPlayer.State.PREPARED) {
                            progressText.text = Converter.formatTime(0L)
                            playButtonControl()
                        }
                    }
                }
            },
            TIMER_UPDATE_DELAY
        )
    }

    private fun setContentToViews() {
        val currentTrack = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CURRENT_TRACK_TAG, Track::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(CURRENT_TRACK_TAG)
        })!!

        MediaPlayer.preparePlayer(currentTrack.previewUrl)
        playButton.setImageResource(R.drawable.player_play_button_100)

        Glide.with(playerMain.context)
            .load(
                currentTrack.getPlayerAlbumImage()
            )
            .placeholder(R.drawable.ic_placeholder_312)
            .fitCenter()
            .transform(
                RoundedCorners(
                    Converter.dpToPx(8.0f, playerMain)
                )
            )
            .into(albumImage)

        titleText.text = currentTrack.trackName
        artistNameText.text = currentTrack.artistName

        progressText.text = Converter.formatTime(0L)

        trackTimeText.text = Converter.formatTime(currentTrack.trackTime)

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

    companion object {
        const val TIMER_UPDATE_DELAY = 300L
    }
}