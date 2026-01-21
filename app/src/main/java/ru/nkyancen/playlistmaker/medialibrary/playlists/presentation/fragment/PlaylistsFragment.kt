package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.nkyancen.playlistmaker.databinding.FragmentMediaLibraryPlaylistsTabBinding
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.PlaylistsState
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.viewmodel.PlaylistsViewModel

class PlaylistsFragment: Fragment() {
    private var _binding: FragmentMediaLibraryPlaylistsTabBinding? = null
    private val binding get() = _binding!!

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

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

        playlistsViewModel.observePlaylistsState().observe(viewLifecycleOwner){
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistsState) {
        when (state) {
            PlaylistsState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.apply {
            mediaPlaylistsList.visibility = View.GONE
            mediaPlaylistsPlaceHolder.visibility = View.VISIBLE
        }
    }

    companion object{
        fun newInstance() = PlaylistsFragment()
    }
}