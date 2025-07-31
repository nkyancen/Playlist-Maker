package ru.nkyancen.playlistmaker.generalView

import android.annotation.SuppressLint
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.searchResultsView.*

class SearchActivity : AppCompatActivity() {
    private var searchText: String = EMPTY_TEXT
    private lateinit var searchEditText: EditText

    private val mediaList = arrayListOf<Track>()

    private val searchAdapter = SearchViewAdapter(mediaList)
    private lateinit var searchRecycler: RecyclerView

    private lateinit var searchPlaceholder: LinearLayout
    private lateinit var searchPlaceholderImage: ImageView
    private lateinit var searchPlaceholderText: TextView
    private lateinit var searchPlaceholderButton: MaterialButton

    private val searchBaseUrl = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(searchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val searchService = retrofit.create(SearchApi::class.java)

    private var searchState: String = CLEAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        val backButton = findViewById<MaterialToolbar>(R.id.searchHeader)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        searchEditText = findViewById(R.id.searchEditText)
        searchRecycler = findViewById(R.id.searchListRecycler)

        searchPlaceholder = findViewById(R.id.searchPlaceholder)
        searchPlaceholderImage = findViewById(R.id.searchPlaceholderImage)
        searchPlaceholderText = findViewById(R.id.searchPlaceholderText)
        searchPlaceholderButton = findViewById(R.id.searchPlaceholderButton)

        backButton.setNavigationOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            clearSearchQuery()
            searchState = CLEAR
            showSearchPlaceholder()
        }

        val searchTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                searchText = s?.toString() ?: EMPTY_TEXT
            }
        }

        searchEditText.addTextChangedListener(searchTextWatcher)

        searchRecycler.adapter = searchAdapter

        searchPlaceholderButton.setOnClickListener {
            searchTracks()
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTracks()
                true
            }
            false
        }
    }

    override fun onResume() {
        super.onResume()
        searchEditText.setText(searchText)
        refreshScreen()
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
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showSearchPlaceholder() {
        if (searchState.isNotEmpty()) {
            mediaList.clear()
            searchAdapter.notifyDataSetChanged()
            searchPlaceholder.visibility = View.VISIBLE
            searchPlaceholderButton.visibility = View.GONE
            when (searchState) {
                NOTHING_FOUND -> {
                    searchPlaceholderImage.setImageResource(R.drawable.ic_placeholder_nothing_found_120)
                    searchPlaceholderText.text = getString(R.string.search_nothing_found)
                    val searchTextCache = searchEditText.text.toString()
                    clearSearchQuery()
                    searchText = searchTextCache
                }

                WITHOUT_INTERNET -> {
                    searchEditText.setText(searchText)
                    searchPlaceholderImage.setImageResource(R.drawable.ic_placeholder_withot_intenet_120)
                    searchPlaceholderText.text = getString(R.string.search_without_internet)
                    searchPlaceholderButton.visibility = View.VISIBLE
                }

                else -> searchPlaceholder.visibility = View.GONE
            }
        } else {
            searchPlaceholder.visibility = View.GONE
        }
    }

    private fun searchTracks() {
        searchService.getTracks(searchText)
            .enqueue(object : Callback<TracksResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TracksResponse?>,
                    response: Response<TracksResponse?>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.tracks?.isNotEmpty() == true) {
                                mediaList.clear()
                                mediaList.addAll(response.body()?.tracks!!)
                                searchAdapter.notifyDataSetChanged()
                                searchState = OK
                            } else {
                                searchState = NOTHING_FOUND
                            }
                        }

                        else -> {
                            searchState = WITHOUT_INTERNET
                        }
                    }
                    showSearchPlaceholder()
                }

                override fun onFailure(
                    call: Call<TracksResponse?>,
                    t: Throwable
                ) {
                    searchState = WITHOUT_INTERNET
                    showSearchPlaceholder()
                }
            })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_REQUEST, EMPTY_TEXT)
        searchState = savedInstanceState.getString(SEARCH_STATE, CLEAR)
        refreshScreen()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshScreen() {
        when (searchState) {
            OK -> searchTracks()
            CLEAR -> {}
            else -> showSearchPlaceholder()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_REQUEST, searchText)
        outState.putString(SEARCH_STATE, searchState)
    }

    companion object {
        const val SEARCH_REQUEST = "Search request"
        const val SEARCH_STATE = "Search state"
        const val EMPTY_TEXT = ""

        const val OK = ""
        const val NOTHING_FOUND = "nothing found"
        const val WITHOUT_INTERNET = "without internet"
        const val CLEAR = "clear"
    }
}