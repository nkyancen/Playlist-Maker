package ru.nkyancen.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<MaterialButton>(R.id.button_search)
        val buttonMedia = findViewById<MaterialButton>(R.id.button_media)
        val buttonSettings = findViewById<MaterialButton>(R.id.button_settings)

        val buttonSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(searchIntent)
            }
        }

        buttonSearch.setOnClickListener(buttonSearchClickListener)

        buttonMedia.setOnClickListener {
            val mediaLibraryIntent = Intent(this@MainActivity, MediaLibraryActivity::class.java)
            startActivity(mediaLibraryIntent)
        }

        buttonSettings.setOnClickListener {
            val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

    }
}