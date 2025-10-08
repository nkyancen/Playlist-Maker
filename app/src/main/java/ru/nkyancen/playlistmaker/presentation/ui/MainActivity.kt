package ru.nkyancen.playlistmaker.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.button.MaterialButton
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.presentation.ui.library.MediaLibraryActivity
import ru.nkyancen.playlistmaker.presentation.ui.search.SearchActivity
import ru.nkyancen.playlistmaker.presentation.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top, bottom = systemBars.bottom)
            insets
        }

        val searchButton = findViewById<MaterialButton>(R.id.searchButton)
        val mediaButton = findViewById<MaterialButton>(R.id.mediaButton)
        val settingsButton = findViewById<MaterialButton>(R.id.settingsButton)

        searchButton.setOnClickListener {
            changeActivity(SearchActivity::class.java)
        }

        mediaButton.setOnClickListener {
            changeActivity(MediaLibraryActivity::class.java)
        }

        settingsButton.setOnClickListener {
            changeActivity(SettingsActivity::class.java)
        }
    }

    private fun changeActivity(target: Class<*>) {
        val targetIntent = Intent(this@MainActivity, target)
        startActivity(targetIntent)
    }
}