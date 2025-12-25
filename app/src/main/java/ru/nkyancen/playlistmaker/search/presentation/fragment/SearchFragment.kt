package ru.nkyancen.playlistmaker.search.presentation.fragment

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.FragmentSearchBinding
import ru.nkyancen.playlistmaker.search.presentation.model.TrackItem
import ru.nkyancen.playlistmaker.search.presentation.model.TracksSearchState
import ru.nkyancen.playlistmaker.search.presentation.viewmodel.SearchViewModel
import ru.nkyancen.playlistmaker.player.presentation.fragment.MediaPlayerFragment

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())

    private var searchText = ""

    private lateinit var searchAdapter: SearchViewAdapter
    private lateinit var historyAdapter: SearchViewAdapter

    private var isClickAllowed = true

    private val viewModel: SearchViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeSearchState().observe(viewLifecycleOwner) {
            render(it)
        }

        setRecyclersAdapters()

        setClickListeners()

        setSearchEditListeners()

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()

        historyAdapter.setData(viewModel.loadHistory())
        historyAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: TracksSearchState) {
        when (state) {
            is TracksSearchState.Default -> showDefault(state.hasFocus)
            is TracksSearchState.ShowContent -> showContent(state.tracks)
            is TracksSearchState.Clear -> clearView(state.history)
            TracksSearchState.EmptyResponse -> showEmpty()
            TracksSearchState.ErrorResponse -> showError()
            TracksSearchState.Loading -> showLoading()
        }
    }

    private fun setRecyclersAdapters() {
        searchAdapter = SearchViewAdapter { onTrackClick(it) }
        binding.searchListRecycler.adapter = searchAdapter

        historyAdapter = SearchViewAdapter { onTrackClick(it) }
        binding.searchHistoryRecycler.adapter = historyAdapter
    }

    private fun setClickListeners() {
        binding.apply {

            searchRequestClearIcon.setOnClickListener {
                clearSearchQuery()
            }

            searchHistoryClearButton.setOnClickListener {
                viewModel.clearHistory()
            }

            searchPlaceholderButton.setOnClickListener {
                viewModel.searchRequest(searchText)
            }
        }
    }

    private fun setSearchEditListeners() {
        binding.searchRequestEdt.setOnFocusChangeListener { _, hasFocus ->
            historyVisibilityChange(hasFocus, searchText)
        }

        binding.searchRequestEdt.doOnTextChanged { s, _, _, _ ->
            binding.searchRequestClearIcon.visibility = setClearButtonVisibility(s)
            searchText = s?.toString() ?: ""
            viewModel.searchDebounce(searchText)
            historyVisibilityChange(binding.searchRequestEdt.hasFocus(), s)
        }
    }

    private fun onTrackClick(track: TrackItem) {
        if (clickDebounce()) {
            viewModel.addToHistory(track)

            findNavController().navigate(
                R.id.action_searchFragment_to_mediaPlayerFragment,
                MediaPlayerFragment.createArgs(track)
            )
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun historyVisibilityChange(hasFocus: Boolean, s: CharSequence?) {
        if (hasFocus
            && s?.isEmpty() == true
        ) {
            viewModel.clearSearchQuery()
        } else {
            binding.searchHistoryLayout.visibility = View.GONE
        }
    }

    private fun setClearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchRequestEdt.windowToken, 0)
    }

    private fun clearSearchQuery() {
        binding.searchRequestEdt.setText("")
        hideKeyboard()
        binding.searchRequestEdt.requestFocus()
        viewModel.clearSearchQuery()
    }

    private fun showDefault(hasFocus: Boolean) {
        binding.apply {
            searchListRecycler.visibility = View.GONE
            searchPlaceholder.visibility = View.GONE
            searchHistoryLayout.visibility = View.GONE
            searchProgressBar.visibility = View.GONE

            if (hasFocus) {
                searchRequestEdt.requestFocus()
            } else {
                searchRequestEdt.clearFocus()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(tracks: List<TrackItem>) {
        binding.apply {
            searchListRecycler.visibility = View.VISIBLE

            searchPlaceholder.visibility = View.GONE
            searchHistoryLayout.visibility = View.GONE
            searchProgressBar.visibility = View.GONE

            searchAdapter.setData(tracks)
            searchAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearView(history: List<TrackItem>) {
        binding.apply {
            searchListRecycler.visibility = View.GONE
            searchPlaceholder.visibility = View.GONE
            searchProgressBar.visibility = View.GONE

            if (history.isEmpty()) {
                searchHistoryLayout.visibility = View.GONE
            } else {
                searchHistoryLayout.visibility = View.VISIBLE

                historyAdapter.setData(history)
                historyAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showEmpty() {
        binding.apply {
            searchPlaceholder.visibility = View.VISIBLE
            searchPlaceholderButton.visibility = View.GONE

            searchHistoryLayout.visibility = View.GONE
            searchListRecycler.visibility = View.GONE
            searchProgressBar.visibility = View.GONE

            searchPlaceholderImage.setImageResource(R.drawable.ic_placeholder_nothing_found_120)
            searchPlaceholderText.text = getString(R.string.search_nothing_found)
            searchRequestEdt.clearFocus()
        }
    }

    private fun showError() {
        binding.apply {
            searchPlaceholder.visibility = View.VISIBLE
            searchPlaceholderButton.visibility = View.VISIBLE

            searchHistoryLayout.visibility = View.GONE
            searchListRecycler.visibility = View.GONE
            searchProgressBar.visibility = View.GONE

            searchRequestEdt.setText(searchText)
            searchPlaceholderImage.setImageResource(R.drawable.ic_placeholder_without_internet_120)
            searchPlaceholderText.text = getString(R.string.search_without_internet)
            searchRequestEdt.clearFocus()
        }
    }

    private fun showLoading() {
        hideKeyboard()
        binding.apply {
            searchProgressBar.visibility = View.VISIBLE

            searchPlaceholder.visibility = View.GONE
            searchPlaceholderButton.visibility = View.GONE
            searchHistoryLayout.visibility = View.GONE
            searchListRecycler.visibility = View.GONE
            searchRequestEdt.clearFocus()
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}