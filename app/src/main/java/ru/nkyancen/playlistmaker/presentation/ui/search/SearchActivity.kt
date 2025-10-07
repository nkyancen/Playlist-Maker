package ru.nkyancen.playlistmaker.presentation.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Runnable
import ru.nkyancen.playlistmaker.Creator
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.domain.consumer.Consumer
import ru.nkyancen.playlistmaker.domain.models.Resource
import ru.nkyancen.playlistmaker.domain.models.Track
import ru.nkyancen.playlistmaker.presentation.mappers.TrackItemMapper
import ru.nkyancen.playlistmaker.presentation.model.TrackItem

class SearchActivity : AppCompatActivity() {

    private val searchRequestRunnable = Runnable { searchTracks() }
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

    private var trackInteractor = Creator.provideTrackInteractor(SEARCH_HISTORY_TAG)
    private val itemMapper = TrackItemMapper()

    private var searchText: String = EMPTY_TEXT
    private lateinit var searchEditText: EditText

    private val mediaList = mutableListOf<TrackItem>()

    private val historyList = mutableListOf<TrackItem>()

    private lateinit var searchAdapter: SearchViewAdapter

    private lateinit var searchResultsList: RecyclerView

    private lateinit var searchPlaceholder: LinearLayout
    private lateinit var searchPlaceholderImage: ImageView
    private lateinit var searchPlaceholderText: TextView
    private lateinit var searchPlaceholderButton: MaterialButton

    private lateinit var backButton: MaterialToolbar
    private lateinit var clearButton: ImageView

    private lateinit var searchProgressBar: ProgressBar

    private lateinit var historyAdapter: SearchViewAdapter
    private lateinit var historyLayout: LinearLayout

    private lateinit var historyClearButton: MaterialButton

    private var searchState = SearchState.CLEAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        initializeViews()

        setRecyclersAdapters()

        setClickListeners()

        setSearchEditListeners()
    }

    override fun onResume() {
        super.onResume()
        searchEditText.setText(searchText)
        updateHistory()
        refreshScreen()
    }

    override fun onDestroy() {
        stopCurrentRunnable()

        super.onDestroy()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        searchText = savedInstanceState.getString(SEARCH_REQUEST, EMPTY_TEXT)
        searchState = SearchState.valueOf(
            savedInstanceState.getString(
                SEARCH_STATE,
                SearchState.CLEAR.name
            )
        )
        refreshScreen()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(SEARCH_REQUEST, searchText)
        outState.putString(SEARCH_STATE, searchState.name)
    }

    private fun initializeViews() {
        backButton = findViewById(R.id.searchHeader)
        clearButton = findViewById(R.id.clearIcon)

        searchEditText = findViewById(R.id.searchEditText)

        searchResultsList = findViewById(R.id.searchListRecycler)
        searchProgressBar = findViewById(R.id.searchProgressBar)

        searchPlaceholder = findViewById(R.id.searchPlaceholder)
        searchPlaceholderImage = findViewById(R.id.searchPlaceholderImage)
        searchPlaceholderText = findViewById(R.id.searchPlaceholderText)
        searchPlaceholderButton = findViewById(R.id.searchPlaceholderButton)

        historyLayout = findViewById(R.id.searchHistoryLayout)
        historyClearButton = findViewById(R.id.searchHistoryClearButton)
    }


    private fun setRecyclersAdapters() {
        val searchRecycler = findViewById<RecyclerView>(R.id.searchListRecycler)
        searchAdapter = SearchViewAdapter(mediaList, SEARCH_HISTORY_TAG)
        searchRecycler.adapter = searchAdapter

        val historyRecycler = findViewById<RecyclerView>(R.id.searchHistoryRecycler)
        historyAdapter = SearchViewAdapter(historyList, SEARCH_HISTORY_TAG)
        historyRecycler.adapter = historyAdapter
        updateHistory()
    }

    private fun setClickListeners() {
        backButton.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            clearSearchQuery()
        }

        historyClearButton.setOnClickListener {
            clearHistory()
        }

        searchPlaceholderButton.setOnClickListener {
            searchTracks()
        }
    }

    private fun setSearchEditListeners() {
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            historyVisibilityChange(hasFocus, searchEditText.text)
        }

        val searchTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchText = s?.toString() ?: EMPTY_TEXT

                if (s?.isEmpty() == false) {
                    searchDebounce()
                } else {
                    searchState = SearchState.CLEAR
                    stopCurrentRunnable()
                }
                historyVisibilityChange(searchEditText.hasFocus(), s)
            }
        }

        searchEditText.addTextChangedListener(searchTextWatcher)
    }

    private fun stopCurrentRunnable() {
        val currentRunnable = searchRunnable
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable)
        }

        handler.removeCallbacks(searchRequestRunnable)
    }

    private fun searchDebounce() {
        stopCurrentRunnable()
        handler.postDelayed(searchRequestRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun historyVisibilityChange(hasFocus: Boolean, s: CharSequence?) {
        historyLayout.visibility =
            if (hasFocus
                && s?.isEmpty() == true
                && historyList.isNotEmpty()
            ) {
                searchResultsList.visibility = View.GONE
                searchPlaceholder.visibility = View.GONE
                View.VISIBLE
            } else {
                if (searchState == SearchState.WITHOUT_INTERNET ||
                    searchState == SearchState.NOTHING_FOUND
                ) {
                    searchPlaceholder.visibility = View.VISIBLE
                }
                View.GONE
            }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clearSearchQuery() {
        searchEditText.setText(EMPTY_TEXT)
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        searchState = SearchState.CLEAR
        showSearchPlaceholder()
        searchEditText.requestFocus()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearHistory() {
        historyList.clear()
        historyAdapter.notifyDataSetChanged()
        historyLayout.visibility = View.GONE
        trackInteractor.savePlayedTracksToHistory(itemMapper.mapListToDomain(historyList))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateHistory() {
        historyList.clear()
        historyList.addAll(itemMapper.mapListToItem(trackInteractor.loadHistoryOfPlayedTracks()))
        historyAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchPlaceholder() {
        if (searchState != SearchState.OK) {
            mediaList.clear()
            searchAdapter.notifyDataSetChanged()
            searchPlaceholder.visibility = View.VISIBLE
            searchPlaceholderButton.visibility = View.GONE
            when (searchState) {
                SearchState.NOTHING_FOUND -> {
                    searchPlaceholderImage.setImageResource(R.drawable.ic_placeholder_nothing_found_120)
                    searchPlaceholderText.text = getString(R.string.search_nothing_found)
                    historyLayout.visibility = View.GONE
                    searchEditText.clearFocus()
                }

                SearchState.WITHOUT_INTERNET -> {
                    searchEditText.setText(searchText)
                    searchPlaceholderImage.setImageResource(R.drawable.ic_placeholder_without_internet_120)
                    searchPlaceholderText.text = getString(R.string.search_without_internet)
                    searchPlaceholderButton.visibility = View.VISIBLE
                    historyLayout.visibility = View.GONE
                    searchEditText.clearFocus()
                    stopCurrentRunnable()
                }

                else -> searchPlaceholder.visibility = View.GONE
            }
        } else {
            searchPlaceholder.visibility = View.GONE
            searchAdapter.notifyDataSetChanged()
            searchResultsList.visibility = View.VISIBLE
        }
    }

    private fun searchTracks() {
        searchPlaceholder.visibility = View.GONE
        historyLayout.visibility = View.GONE
        searchResultsList.visibility = View.GONE
        searchProgressBar.visibility = View.VISIBLE

        provideSearchRequest()
    }

    private fun provideSearchRequest() {
        trackInteractor.searchTracks(
            searchText,
            object : Consumer {
                override fun consume(tracks: Resource<List<Track>?>) {

                    val newSearchRunnable = Runnable {
                        searchProgressBar.visibility = View.GONE
                        if (tracks.data != null && tracks.expression == searchText) {
                            if (tracks.data.isNotEmpty()) {
                                mediaList.clear()
                                mediaList.addAll(itemMapper.mapListToItem(tracks.data))
                                searchState = SearchState.OK
                            } else {
                                searchState = SearchState.NOTHING_FOUND
                            }
                        } else if (tracks.data == null) {
                            searchState = SearchState.WITHOUT_INTERNET
                        } else {
                            stopCurrentRunnable()
                        }
                        showSearchPlaceholder()
                    }

                    searchRunnable = newSearchRunnable
                    handler.post(newSearchRunnable)
                }
            }
        )
    }

    private fun refreshScreen() {
        when (searchState) {
            SearchState.OK -> searchTracks()
            else -> showSearchPlaceholder()
        }
    }

    companion object {
        const val EMPTY_TEXT = ""

        const val SEARCH_REQUEST = "Search request"
        const val SEARCH_STATE = "Search state"
        const val SEARCH_HISTORY_TAG = "Search History"

        const val SEARCH_DEBOUNCE_DELAY = 2_000L

        enum class SearchState {
            OK,
            CLEAR,
            NOTHING_FOUND,
            WITHOUT_INTERNET;
        }
    }
}