package ru.nkyancen.playlistmaker.ui.medialibrary

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.nkyancen.playlistmaker.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            mediaLibraryHeader.setNavigationOnClickListener {
                finish()
            }
        }
    }
}