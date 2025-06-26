package ru.nkyancen.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<MaterialButton>(R.id.button_search)
        val buttonMedia = findViewById<MaterialButton>(R.id.button_media)
        val buttonSettings = findViewById<MaterialButton>(R.id.button_settings)

        buttonSearch.setOnClickListener {
            changeActivity(SearchActivity::class.java)
        }

        buttonMedia.setOnClickListener {
            changeActivity(MediaLibraryActivity::class.java)
        }

        buttonSettings.setOnClickListener {
            changeActivity(SettingsActivity::class.java)
        }
    }

    private fun changeActivity(target: Class<*>) {
        val targetIntent = Intent(this@MainActivity, target)
        startActivity(targetIntent)
    }
}