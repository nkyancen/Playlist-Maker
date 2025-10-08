package ru.nkyancen.playlistmaker.presentation.ui.player

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.appbar.MaterialToolbar
import ru.nkyancen.playlistmaker.Creator
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.presentation.model.TrackItem
import ru.nkyancen.playlistmaker.presentation.utils.Converter

const val CURRENT_TRACK_TAG = "Current Track"

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
    private val mediaPlayerInteractor = Creator.providePlayerInteractor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updatePadding(top = statusBar.top)
            insets
        }


        initializeViews()

        setContentToViews()

        setClickListeners()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayerInteractor.pause()
    }

    override fun onRestart() {
        super.onRestart()
        setPlayButtonImage()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerInteractor.release()
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
            mediaPlayerInteractor.playbackControl()

            setPlayButtonImage()
            provideTimer()
        }
    }

    private fun setPlayButtonImage() {
        if (mediaPlayerInteractor.isPlaying()) {
            playButton.setImageResource(R.drawable.player_pause_button_100)
        } else {
            playButton.setImageResource(R.drawable.player_play_button_100)
        }
    }

    private fun provideTimer() {
        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    if (mediaPlayerInteractor.isPlaying()) {
                        progressText.text = Converter.formatTime(
                            mediaPlayerInteractor.getCurrentPosition().toLong()
                        )

                        handler.postDelayed(
                            this,
                            TIMER_UPDATE_DELAY
                        )
                    } else {
                        handler.removeCallbacks(this)

                        if (mediaPlayerInteractor.isPrepared()) {
                            progressText.text = Converter.formatTime(0L)
                            setPlayButtonImage()
                        }
                    }
                }
            },
            TIMER_UPDATE_DELAY
        )
    }

    private fun setContentToViews() {
        val currentTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CURRENT_TRACK_TAG, TrackItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(CURRENT_TRACK_TAG)
        }!!

        mediaPlayerInteractor.prepare(currentTrack.preview)
        playButton.setImageResource(R.drawable.player_play_button_100)

        Glide.with(playerMain.context)
            .load(
                currentTrack.getPlayerAlbumPoster()
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

        if (currentTrack.albumName.isEmpty()) {
            trackAlbumGroup.visibility = View.GONE
        } else {
            trackAlbumGroup.visibility = View.VISIBLE
            trackAlbumText.text = currentTrack.albumName
        }

        if (currentTrack.releaseYear.isEmpty()) {
            trackYearGroup.visibility = View.GONE
        } else {
            trackYearGroup.visibility = View.VISIBLE
            trackYearText.text = currentTrack.releaseYear
        }

        trackGenreText.text = currentTrack.genre

        trackCountryText.text = currentTrack.country
    }

    companion object {
        const val TIMER_UPDATE_DELAY = 300L
    }
}