package ru.nkyancen.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_search)

        val backButton = findViewById<MaterialToolbar>(R.id.searchHeader)

        backButton.setNavigationOnClickListener {
            finish()
        }

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val searchEditText = findViewById<EditText>(R.id.searchEditText)


        clearButton.setOnClickListener {
            searchEditText.setText("")
        }

        val searchTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
            }
        }

        searchEditText.addTextChangedListener(searchTextWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

}