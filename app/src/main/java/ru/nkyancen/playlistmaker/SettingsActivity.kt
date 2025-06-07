package ru.nkyancen.playlistmaker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        val buttonBack = findViewById<Button>(R.id.button_back)

        buttonBack.setOnClickListener {
            finish()
        }
    }
}