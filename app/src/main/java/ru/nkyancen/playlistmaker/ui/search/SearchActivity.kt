package ru.nkyancen.playlistmaker.ui.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.core.creator.Creator
import ru.nkyancen.playlistmaker.databinding.ActivitySearchBinding
import ru.nkyancen.playlistmaker.presentation.search.model.TrackItem
import ru.nkyancen.playlistmaker.presentation.search.model.TracksSearchState
import ru.nkyancen.playlistmaker.presentation.search.viewmodel.SearchViewModel
import ru.nkyancen.playlistmaker.ui.player.MediaPlayerActivity

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private val handler = Handler(Looper.getMainLooper())

    private var searchText: String = EMPTY_TEXT

    private lateinit var searchAdapter: SearchViewAdapter
    private lateinit var historyAdapter: SearchViewAdapter

    private var isClickAllowed = true

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider.create(this, SearchViewModel.getFactory())
            .get(SearchViewModel::class.java)
        viewModel.observeSearchState().observe(this) {
            render(it)
        }

        setRecyclersAdapters()

        setClickListeners()

        setSearchEditListeners()
    }

    override fun onResume() {
        super.onResume()
        binding.searchRequestEdt.setText(searchText)
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
            searchHeader.setNavigationOnClickListener {
                finish()
            }

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
                searchText = s?.toString() ?: EMPTY_TEXT
                viewModel.searchDebounce(searchText)
                historyVisibilityChange(binding.searchRequestEdt.hasFocus(), s)
        }
    }

    private fun onTrackClick(track: TrackItem) {
        if (clickDebounce()) {
            val targetIntent = Intent(this, MediaPlayerActivity::class.java)
            targetIntent.putExtra(Creator.CURRENT_TRACK_TAG, track)

            viewModel.addToHistory(track)
            startActivity(targetIntent)
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
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.searchRequestEdt.windowToken, 0)
    }

    private fun clearSearchQuery() {
        binding.searchRequestEdt.setText(EMPTY_TEXT)
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
        const val EMPTY_TEXT = ""

        const val CLICK_DEBOUNCE_DELAY = 1_000L
    }
}