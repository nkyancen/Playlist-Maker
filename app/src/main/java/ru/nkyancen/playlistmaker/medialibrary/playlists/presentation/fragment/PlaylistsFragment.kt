package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.FragmentMediaLibraryPlaylistsTabBinding
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistItem
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistsState
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.viewmodel.PlaylistsViewModel

class PlaylistsFragment : Fragment() {
    private var _binding: FragmentMediaLibraryPlaylistsTabBinding? = null
    private val binding get() = _binding!!

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private lateinit var playlistsAdapter: PlaylistViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaLibraryPlaylistsTabBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsViewModel.observePlaylistsState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.mediaCreateNewPlaylistButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_newPlaylistFragment
            )
        }

        binding.mediaPlaylistsList.layoutManager = GridLayoutManager(requireContext(), 2)
        playlistsAdapter = PlaylistViewAdapter() {
            playlistsViewModel.getUriForCover(it)
        }
        binding.mediaPlaylistsList.adapter = playlistsAdapter

        playlistsViewModel.showPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            PlaylistsState.Empty -> showEmpty()
            PlaylistsState.Loading -> showLoading()
            is PlaylistsState.Content -> showContent(state.playlists)
        }
    }

    private fun showEmpty() {
        binding.apply {
            mediaPlaylistsList.visibility = View.GONE
            mediaPlaylistsPlaceHolder.visibility = View.VISIBLE
            mediaPlaylistProgressBar.visibility = View.GONE
        }
    }

    private fun showLoading() {
        binding.apply {
            mediaPlaylistsList.visibility = View.GONE
            mediaPlaylistsPlaceHolder.visibility = View.GONE
            mediaPlaylistProgressBar.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(playlists: List<PlaylistItem>) {
        binding.apply {
            mediaPlaylistsList.visibility = View.VISIBLE
            mediaPlaylistsPlaceHolder.visibility = View.GONE
            mediaPlaylistProgressBar.visibility = View.GONE

            playlistsAdapter.setData(playlists)
            playlistsAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}