package ru.nkyancen.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

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