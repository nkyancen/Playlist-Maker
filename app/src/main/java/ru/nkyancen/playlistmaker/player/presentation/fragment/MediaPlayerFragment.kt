package ru.nkyancen.playlistmaker.player.presentation.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.core.utils.Converter
import ru.nkyancen.playlistmaker.databinding.FragmentMediaPlayerBinding
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem
import ru.nkyancen.playlistmaker.player.presentation.fragment.playlists.PlayerPlaylistViewAdapter
import ru.nkyancen.playlistmaker.player.presentation.model.BottomSheetState
import ru.nkyancen.playlistmaker.player.presentation.model.PlayerState
import ru.nkyancen.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class MediaPlayerFragment : Fragment(), KoinComponent {
    private var _binding: FragmentMediaPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlayerViewModel

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var playlistAdapter: PlayerPlaylistViewAdapter

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
            parametersOf(currentTrack.id, currentTrack.preview)
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            setPlayButtonImage(it is PlayerState.Play)
            setFavoriteButtonImage(it.isFavorites)
            binding.playerProgressTimeText.text = it.progressTime
        }

        viewModel.observeBottomSheetState().observe(viewLifecycleOwner) {
            renderBottomSheet(it)
        }

        viewModel.observeShowMessage().observe(viewLifecycleOwner) {
            val message = if (it.second) {
                getString(R.string.message_add_to_playlist, it.first)
            } else {
                getString(R.string.message_track_already_in_playlist, it.first)
            }
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }

        val playlistClickListener = PlayerPlaylistViewAdapter.PlaylistClickListener { playlist ->
            viewModel.onPlaylistClick(currentTrack.id, playlist)
        }

        val externalInteractor = PlayerPlaylistViewAdapter.ExternalInteractor { coverName ->
            viewModel.getUriForCover(coverName)
        }

        binding.playerPlaylistRecycler.layoutManager = LinearLayoutManager(requireContext())
        playlistAdapter = PlayerPlaylistViewAdapter(
            externalInteractor,
            playlistClickListener
        )
        binding.playerPlaylistRecycler.adapter = playlistAdapter

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playerBottomSheetContainer)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset >= -0.85) {
                    binding.playerBlackOut.visibility = View.VISIBLE
                } else {
                    binding.playerBlackOut.visibility = View.GONE
                }
            }

        })

        setClickListeners(currentTrack)

        setContentToViews(currentTrack)
    }

    private fun renderBottomSheet(state: BottomSheetState) {
        when (state) {
            BottomSheetState.Hide -> hideBottomSheet()
            is BottomSheetState.Show -> showBottomSheet(state.playlists)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showBottomSheet(playlists: List<PlaylistItem>) {
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.playerBlackOut.apply {
            isClickable = true
            isEnabled = true
        }

        playlistAdapter.setData(playlists)
        playlistAdapter.notifyDataSetChanged()
    }

    private fun hideBottomSheet() {
        bottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.playerBlackOut.apply {
            isClickable = false
            isEnabled = false
        }
    }


    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListeners(track: TrackItem) {
        binding.apply {
            playerHeader.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            playerPlayButton.setOnClickListener {
                viewModel.onPlayButtonClicked()
            }

            playerAddToFavorites.setOnClickListener {
                viewModel.onFavoriteButtonClicked(track)
            }

            playerAddToPlayList.setOnClickListener {
                viewModel.showBottomSheet()
            }

            playerBlackOut.setOnClickListener {
                viewModel.hideBottomSheet()
            }

            playerCreateNewPlaylistButton.setOnClickListener {
                findNavController().navigate(
                    R.id.action_mediaPlayerFragment_to_newPlaylistFragment
                )
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

    private fun setFavoriteButtonImage(isFavorites: Boolean) {
        if (isFavorites) {
            binding.playerAddToFavorites.setImageResource(R.drawable.ic_player_favorite_24)
        } else {
            binding.playerAddToFavorites.setImageResource(R.drawable.ic_player_not_favorite_24)
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