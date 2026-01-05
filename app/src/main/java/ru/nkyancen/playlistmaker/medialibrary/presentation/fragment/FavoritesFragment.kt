package ru.nkyancen.playlistmaker.medialibrary.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.nkyancen.playlistmaker.databinding.FragmentMediaLibraryFavoritesTabBinding
import ru.nkyancen.playlistmaker.medialibrary.presentation.model.FavoritesState
import ru.nkyancen.playlistmaker.medialibrary.presentation.viewmodel.FavoritesViewModel

class FavoritesFragment : Fragment() {
    private var _binding: FragmentMediaLibraryFavoritesTabBinding? = null
    private val binding get() = _binding!!

    private val favoritesViewModel : FavoritesViewModel by viewModel()

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

        favoritesViewModel.observeFavoritesState().observe(viewLifecycleOwner){
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: FavoritesState) {
        when (state) {
            FavoritesState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
        binding.apply {
            mediaFavoritesList.visibility = View.GONE
            mediaFavoritesPlaceHolder.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}