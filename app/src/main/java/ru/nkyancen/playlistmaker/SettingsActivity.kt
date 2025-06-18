package ru.nkyancen.playlistmaker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_setting)

        val buttonBack = findViewById<MaterialToolbar>(R.id.header_settings)

        buttonBack.setNavigationOnClickListener {
            finish()
        }
    }
}