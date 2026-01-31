package ru.nkyancen.playlistmaker.medialibrary.favorites.presentation.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.core.utils.debounce
import ru.nkyancen.playlistmaker.databinding.FragmentMediaLibraryFavoritesTabBinding
import ru.nkyancen.playlistmaker.medialibrary.favorites.presentation.model.FavoritesState
import ru.nkyancen.playlistmaker.medialibrary.favorites.presentation.viewmodel.FavoritesViewModel
import ru.nkyancen.playlistmaker.player.presentation.fragment.MediaPlayerFragment
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem

class FavoritesFragment : Fragment() {
    private var _binding: FragmentMediaLibraryFavoritesTabBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel: FavoritesViewModel by viewModel()

    private lateinit var onTrackClickDebounce: (TrackItem) -> Unit

    private lateinit var favoritesAdapter: FavoritesViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaLibraryFavoritesTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoritesViewModel.observeFavoritesState().observe(viewLifecycleOwner) {
            render(it)
        }

        onTrackClickDebounce = debounce<TrackItem>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_mediaPlayerFragment,
                MediaPlayerFragment.createArgs(track)
            )
        }

        binding.mediaFavoritesList.layoutManager = LinearLayoutManager(requireContext())
        favoritesAdapter = FavoritesViewAdapter { onTrackClickDebounce(it) }
        binding.mediaFavoritesList.adapter = favoritesAdapter

        favoritesViewModel.showFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: FavoritesState) {
        when (state) {
            FavoritesState.Empty -> showEmpty()
            FavoritesState.Loading -> showLoading()
            is FavoritesState.Content -> showContent(state.favorites)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(favorites: List<TrackItem>) {
        binding.apply {
            mediaFavoritesList.visibility = View.VISIBLE
            mediaFavoritesProgressBar.visibility = View.GONE
            mediaFavoritesPlaceHolder.visibility = View.GONE

            favoritesAdapter.setData(favorites)
            favoritesAdapter.notifyDataSetChanged()
        }
    }

    private fun showLoading() {
        binding.apply {
            mediaFavoritesList.visibility = View.GONE
            mediaFavoritesProgressBar.visibility = View.VISIBLE
            mediaFavoritesPlaceHolder.visibility = View.GONE
        }
    }

    private fun showEmpty() {
        binding.apply {
            mediaFavoritesList.visibility = View.GONE
            mediaFavoritesProgressBar.visibility = View.GONE
            mediaFavoritesPlaceHolder.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }

}