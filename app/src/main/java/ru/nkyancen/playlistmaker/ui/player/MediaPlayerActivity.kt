package ru.nkyancen.playlistmaker.ui.player

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.databinding.ActivityMediaPlayerBinding
import ru.nkyancen.playlistmaker.presentation.player.model.PlayerState
import ru.nkyancen.playlistmaker.presentation.player.viewmodel.PlayerViewModel
import ru.nkyancen.playlistmaker.presentation.search.model.TrackItem
import ru.nkyancen.playlistmaker.ui.search.SearchActivity.Companion.CURRENT_TRACK_TAG

class MediaPlayerActivity : AppCompatActivity(), KoinComponent {
    private lateinit var binding: ActivityMediaPlayerBinding

    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaPlayerBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tag = CURRENT_TRACK_TAG
        val currentTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(tag, TrackItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(tag)
        }!!

        viewModel = getKoin().get {
            parametersOf(currentTrack.preview)
        }

        viewModel.observeProgressTime().observe(this) {
            binding.playerProgressTimeText.text = it
        }

        viewModel.observePlayerState().observe(this) {
            setPlayButtonImage(it == PlayerState.Play)
        }

        setContentToViews(currentTrack)

        setClickListeners()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun setClickListeners() {
        binding.apply {
            playerHeader.setNavigationOnClickListener {
                finish()
            }
        }

        binding.playerPlayButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
    }

    private fun setPlayButtonImage(isPlaying: Boolean) {
        if (isPlaying) {
            binding.playerPlayButton.setImageResource(R.drawable.player_pause_button_100)
        } else {
            binding.playerPlayButton.setImageResource(R.drawable.player_play_button_100)
        }
    }

    private fun setContentToViews(currentTrack: TrackItem) {

        Glide.with(binding.root)
            .load(
                currentTrack.getPlayerAlbumPoster()
            )
            .placeholder(R.drawable.ic_placeholder_312)
            .fitCenter()
            .transform(
                RoundedCorners(
                    Converter.dpToPx(8.0f, binding.root)
                )
            )
            .into(binding.playerAlbumImage)

        binding.apply {
            playerPlayButton.setImageResource(R.drawable.player_play_button_100)

            playerTitleText.text = currentTrack.trackName
            playerArtistText.text = currentTrack.artistName

            playerProgressTimeText.text = Converter.formatTime(0L)

            playerTrackTimeText.text = Converter.formatTime(currentTrack.trackTime)

            if (currentTrack.albumName.isEmpty()) {
                playerTrackAlbumGroup.visibility = View.GONE
            } else {
                playerTrackAlbumGroup.visibility = View.VISIBLE
                playerTrackAlbumText.text = currentTrack.albumName
            }

            if (currentTrack.releaseYear.isEmpty()) {
                playerTrackYearGroup.visibility = View.GONE
            } else {
                playerTrackYearGroup.visibility = View.VISIBLE
                playerTrackYearText.text = currentTrack.releaseYear
            }

            playerTrackGenreText.text = currentTrack.genre

            playerTrackCountryText.text = currentTrack.country
        }
    }
}