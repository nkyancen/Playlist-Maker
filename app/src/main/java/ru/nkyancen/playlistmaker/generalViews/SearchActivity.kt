package ru.nkyancen.playlistmaker.generalViews

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.model.*
import ru.nkyancen.playlistmaker.searchResults.*


class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_REQUEST = "Search request"
        const val SEARCH_STATE = "Search state"
        const val EMPTY_TEXT = ""
    }

    private var searchText: String = EMPTY_TEXT
    private lateinit var searchEditText: EditText

    private val mediaList = arrayListOf<Track>()

    private val historyList = arrayListOf<Track>()

    private lateinit var searchAdapter: SearchViewAdapter

    private lateinit var searchPlaceholder: LinearLayout
    private lateinit var searchPlaceholderImage: ImageView
    private lateinit var searchPlaceholderText: TextView
    private lateinit var searchPlaceholderButton: MaterialButton

    private lateinit var backButton: MaterialToolbar
    private lateinit var clearButton: ImageView

    private lateinit var historyAdapter: SearchViewAdapter
    private lateinit var historyLayout: LinearLayout

    private lateinit var historyClearButton: MaterialButton

    private lateinit var historySearchUser: SearchHistory
    private lateinit var historyListener: SharedPreferences.OnSharedPreferenceChangeListener

    private var searchState = SearchState.CLEAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        initializeViews()

        initializeHistoryUser()

        setRecyclersAdapters()

        setClickListeners()

        setSearchEditListeners()
    }

    override fun onResume() {
        super.onResume()
        searchEditText.setText(searchText)
        refreshScreen()
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

        searchPlaceholder = findViewById(R.id.searchPlaceholder)
        searchPlaceholderImage = findViewById(R.id.searchPlaceholderImage)
        searchPlaceholderText = findViewById(R.id.searchPlaceholderText)
        searchPlaceholderButton = findViewById(R.id.searchPlaceholderButton)

        historyLayout = findViewById(R.id.searchHistoryLayout)
        historyClearButton = findViewById(R.id.searchHistoryClearButton)
    }

    private fun initializeHistoryUser() {
        val historySharedPrefs = getSharedPreferences(SEARCH_HISTORY_TAG, MODE_PRIVATE)

        historyListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefs, key ->
                if (key == SEARCH_HISTORY_TAG) {
                    updateHistory()
                }
            }

        historySharedPrefs.registerOnSharedPreferenceChangeListener(historyListener)

        historySearchUser = SearchHistory(historySharedPrefs)
    }

    private fun setRecyclersAdapters() {
        val searchRecycler = findViewById<RecyclerView>(R.id.searchListRecycler)
        searchAdapter = SearchViewAdapter(mediaList, historySearchUser)
        searchRecycler.adapter = searchAdapter

        val historyRecycler = findViewById<RecyclerView>(R.id.searchHistoryRecycler)
        historyAdapter = SearchViewAdapter(historyList, historySearchUser)
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
                historyVisibilityChange(searchEditText.hasFocus(), s)

                clearButton.visibility = clearButtonVisibility(s)
                searchText = s?.toString() ?: EMPTY_TEXT
            }
        }

        searchEditText.addTextChangedListener(searchTextWatcher)

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTracks()
                true
            }
            false
        }
    }

    private fun historyVisibilityChange(hasFocus: Boolean, s: CharSequence?) {
        historyLayout.visibility =
            if (hasFocus
                && s?.isEmpty() == true
                && historyList.isNotEmpty()
            ) {
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
        historySearchUser.write(historyList)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateHistory() {
        historyList.clear()
        historyList.addAll(historySearchUser.read())
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
                }

                else -> searchPlaceholder.visibility = View.GONE
            }
        } else {
            searchPlaceholder.visibility = View.GONE
        }
    }

    private fun searchTracks() {
        SearchService().service.getTracks(searchText)
            .enqueue(object : Callback<TracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TracksResponse?>,
                    response: Response<TracksResponse?>
                ) {
                    if (response.isSuccessful) {
                        val currentResponse = response.body()?.tracks
                        if (!currentResponse.isNullOrEmpty()) {
                            mediaList.clear()
                            mediaList.addAll(currentResponse)
                            searchAdapter.notifyDataSetChanged()
                            searchState = SearchState.OK
                        } else {
                            searchState = SearchState.NOTHING_FOUND
                        }
                    } else {
                        searchState = SearchState.WITHOUT_INTERNET
                    }
                    showSearchPlaceholder()
                }

                override fun onFailure(
                    call: Call<TracksResponse?>,
                    t: Throwable
                ) {
                    searchState = SearchState.WITHOUT_INTERNET
                    showSearchPlaceholder()
                }
            })
    }

    private fun refreshScreen() {
        when (searchState) {
            SearchState.OK -> searchTracks()
            SearchState.CLEAR -> {}
            else -> showSearchPlaceholder()
        }
    }
}
