package ru.nkyancen.playlistmaker.player.presentation.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.databinding.FragmentMediaPlayerBinding
import ru.nkyancen.playlistmaker.player.presentation.model.PlayerState
import ru.nkyancen.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class MediaPlayerFragment : Fragment(), KoinComponent {
    private var _binding: FragmentMediaPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaPlayerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(CURRENT_TRACK_TAG, TrackItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(CURRENT_TRACK_TAG)
        }!!

        viewModel = getKoin().get {
            parametersOf(currentTrack.preview)
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            setPlayButtonImage(it is PlayerState.Play)
            binding.playerProgressTimeText.text = it.progressTime
        }

        setClickListeners()

        setContentToViews(currentTrack)
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListeners() {
        binding.apply {
            playerHeader.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            playerPlayButton.setOnClickListener {
                viewModel.onPlayButtonClicked()
            }
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
            .error(R.drawable.ic_placeholder_312)
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

    companion object {
        private const val CURRENT_TRACK_TAG = "Current Track"

        fun createArgs(track: TrackItem): Bundle =
            bundleOf(CURRENT_TRACK_TAG to track)
    }
}