package ru.nkyancen.playlistmaker.playlist_detail.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem
import ru.nkyancen.playlistmaker.player.presentation.fragment.MediaPlayerFragment
import ru.nkyancen.playlistmaker.playlist_detail.presentation.model.PlaylistDetailsBottomSheetState
import ru.nkyancen.playlistmaker.playlist_detail.presentation.model.PlaylistDetailsMenuBottomSheetState
import ru.nkyancen.playlistmaker.playlist_detail.presentation.model.PlaylistDetailsState
import ru.nkyancen.playlistmaker.playlist_detail.presentation.viewmodel.PlaylistDetailsViewModel
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem


class PlaylistDetailsFragment : Fragment() {
    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlaylistDetailsViewModel

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var playlistDetailsAdapter: PlaylistDetailsItemViewAdapter

    private lateinit var deleteTrackDialog: MaterialAlertDialogBuilder

    private lateinit var deletePlaylistDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentPlaylistId = arguments?.getLong(CURRENT_PLAYLIST_ID_TAG)!!

        viewModel = getKoin().get() {
            parametersOf(currentPlaylistId)
        }
        setViewModelObserves()

        viewModel.updatePlaylistDetails()

        setBottomSheets()

        setDeletePlaylistDialog(currentPlaylistId)

        setButtonClickListeners(currentPlaylistId)

        setTrackListRecyclerAdapter()
    }

    override fun onResume() {
        super.onResume()

        viewModel.updatePlaylistDetails()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViewModelObserves() {
        viewModel.observePlaylistDetailsState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observePlaylistDetailsBottomSheetState().observe(viewLifecycleOwner) {
            renderBottomSheet(it)
        }

        viewModel.observePlaylistDetailsMenuBottomSheetState().observe(viewLifecycleOwner) {
            renderBottomSheetMenu(it)
        }

        viewModel.observeShowMessage().observe(viewLifecycleOwner) {
            Snackbar
                .make(binding.root, R.string.has_not_share_tracks, Snackbar.LENGTH_LONG)
                .setBackgroundTint(resources.getColor(R.color.snackbar_background, null))
                .setTextColor(resources.getColor(R.color.white, null))
                .show()
        }
    }

    private fun setBottomSheets() {
        val screenHeight = requireActivity().resources.displayMetrics.heightPixels

        bottomSheetBehavior = BottomSheetBehavior.from(binding.playerDetailsBottomSheetContainer)


        bottomSheetBehavior.peekHeight = (screenHeight * 0.3).toInt()

        menuBottomSheetBehavior =
            BottomSheetBehavior.from(binding.playerDetailsMenuBottomSheetContainer)

        menuBottomSheetBehavior.peekHeight = (screenHeight * 0.48).toInt()
        menuBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset >= -0.85) {
                    binding.playlistDetailsBlackOut.visibility = View.VISIBLE
                } else {
                    binding.playlistDetailsBlackOut.visibility = View.GONE
                }
            }
        })
    }

    private fun setDeleteTrackDialog(track: TrackItem) {
        deleteTrackDialog = MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(getString(R.string.delete_track_confirmation))
            .setNegativeButton(getString(R.string.delete_track_confirmation_negative), null)
            .setPositiveButton(getString(R.string.delete_track_confirmation_positive)) { _, _ ->
                viewModel.deleteTrackFromPlaylist(track)
            }
    }

    private fun setDeletePlaylistDialog(playlistId: Long) {
        deletePlaylistDialog = MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(getString(R.string.delete_playlist_confirmation_title))
            .setMessage(getString(R.string.delete_playlist_confirmation_body))
            .setNegativeButton(getString(R.string.delete_playlist_confirmation_negative), null)
            .setPositiveButton(getString(R.string.delete_playlist_confirmation_positive)) { _, _ ->

                viewModel.deletePlaylist(playlistId)

                findNavController().navigateUp()
            }
    }

    private fun setTrackListRecyclerAdapter() {
        binding.playlistDetailsTracksBottomSheetRecycler.layoutManager =
            LinearLayoutManager(requireContext())
        playlistDetailsAdapter = PlaylistDetailsItemViewAdapter({ track ->
            findNavController().navigate(
                R.id.action_playlistDetailsFragment_to_mediaPlayerFragment,
                MediaPlayerFragment.createArgs(track)
            )
        }, { track ->
            setDeleteTrackDialog(track)
            deleteTrackDialog.show()
        })
        binding.playlistDetailsTracksBottomSheetRecycler.adapter = playlistDetailsAdapter
    }

    private fun setButtonClickListeners(playlistId: Long) {
        binding.apply {
            playlistDetailsHeader.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            playlistDetailsShareButton.setOnClickListener {
                viewModel.sharePlaylist(playlistId)
            }

            playlistDetailsAdditionalMenuButton.setOnClickListener {
                viewModel.showMenu()
            }

            playlistDetailsBlackOut.setOnClickListener {
                viewModel.hideMenu()
            }

            playlistDetailsMenuShareButton.setOnClickListener {
                viewModel.sharePlaylist(playlistId)
            }

            playlistDetailsMenuDeleteButton.setOnClickListener {
                hideMenu()
                deletePlaylistDialog.show()
            }

            playlistDetailsMenuEditButton.setOnClickListener {
                findNavController().navigate(
                    R.id.action_playlistDetailsFragment_to_playlistEditorFragment,
                    PlaylistEditorFragment.createArgs(playlistId)
                )
            }
        }
    }

    private fun render(state: PlaylistDetailsState) {
        when (state) {
            is PlaylistDetailsState.Content -> setContentToViews(
                state.playlist,
                state.totalDuration
            )

            PlaylistDetailsState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.apply {
            playlistDetailsProgressBar.visibility = View.VISIBLE
            playlistDetailsContainer.visibility = View.GONE
        }
    }

    private fun setContentToViews(currentPlaylist: PlaylistItem, totalProgressTime: Int) {
        val coverUri = viewModel.getUriForCover(currentPlaylist.coverImage)

        binding.apply {
            playlistDetailsProgressBar.visibility = View.GONE
            playlistDetailsContainer.visibility = View.VISIBLE

            if (coverUri != null) {
                playlistDetailsCover.setImageURI(coverUri)
                playlistDetailsMenuCover.setImageURI(coverUri)
            } else {
                playlistDetailsCover.setImageResource(R.drawable.ic_placeholder_360)
                playlistDetailsMenuCover.setImageResource(R.drawable.ic_placeholder_100)
            }

            playlistDetailsTitle.text = currentPlaylist.title
            playlistDetailsDescription.text = currentPlaylist.description

            playlistDetailsDurations.text = requireContext().resources.getQuantityString(
                R.plurals.total_playback_time,
                totalProgressTime,
                totalProgressTime
            )

            playlistDetailsTracksAmount.text = requireContext().resources.getQuantityString(
                R.plurals.track_amount,
                currentPlaylist.tracksAmount,
                currentPlaylist.tracksAmount
            )


            playlistDetailsMenuTitle.text = currentPlaylist.title

            playlistDetailsMenuTracksAmount.text = requireContext().resources.getQuantityString(
                R.plurals.track_amount,
                currentPlaylist.tracksAmount,
                currentPlaylist.tracksAmount
            )
        }
    }

    private fun renderBottomSheet(state: PlaylistDetailsBottomSheetState) {
        when (state) {
            PlaylistDetailsBottomSheetState.Loading -> showBottomSheetLoading()
            is PlaylistDetailsBottomSheetState.Content -> setContentToBottomSheet(state.tracks)
            PlaylistDetailsBottomSheetState.Empty -> showEmpty()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setContentToBottomSheet(tracks: List<TrackItem>) {
        binding.apply {
            playlistDetailsBottomSheetProgressBar.visibility = View.GONE
            playlistDetailsTracksBottomSheetRecycler.visibility = View.VISIBLE
            playlistDetailPlaceholder.visibility = View.GONE

            playlistDetailsAdapter.setData(tracks)
            playlistDetailsAdapter.notifyDataSetChanged()
        }
    }

    private fun showBottomSheetLoading() {
        binding.apply {
            playlistDetailsBottomSheetProgressBar.visibility = View.VISIBLE
            playlistDetailsTracksBottomSheetRecycler.visibility = View.GONE
            playlistDetailPlaceholder.visibility = View.GONE
        }
    }

    private fun showEmpty() {
        binding.apply {
            playlistDetailsBottomSheetProgressBar.visibility = View.GONE
            playlistDetailsTracksBottomSheetRecycler.visibility = View.GONE
            playlistDetailPlaceholder.visibility = View.VISIBLE
        }
    }

    private fun renderBottomSheetMenu(state: PlaylistDetailsMenuBottomSheetState) {
        when (state) {
            PlaylistDetailsMenuBottomSheetState.Hide -> hideMenu()
            PlaylistDetailsMenuBottomSheetState.Show -> showMenu()
        }
    }

    private fun showMenu() {
        menuBottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.playlistDetailsBlackOut.apply {
            isClickable = true
            isEnabled = true
        }
    }

    private fun hideMenu() {
        menuBottomSheetBehavior.apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.playlistDetailsBlackOut.apply {
            isClickable = false
            isEnabled = false
        }
    }

    companion object {
        private const val CURRENT_PLAYLIST_ID_TAG = "Current PlaylistId"

        fun createArgs(playlistId: Long): Bundle =
            bundleOf(CURRENT_PLAYLIST_ID_TAG to playlistId)
    }
}