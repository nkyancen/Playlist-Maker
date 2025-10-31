package ru.nkyancen.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.nkyancen.playlistmaker.databinding.ActivityMainBinding
import ru.nkyancen.playlistmaker.presentation.main.model.MainEvent
import ru.nkyancen.playlistmaker.presentation.main.viewmodel.MainViewModel
import ru.nkyancen.playlistmaker.ui.medialibrary.MediaLibraryActivity
import ru.nkyancen.playlistmaker.ui.search.SearchActivity
import ru.nkyancen.playlistmaker.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.observeState().observe(this) {
            transitToActivity(it)
        }

        binding.apply {
            mainSearchButton.setOnClickListener {
                viewModel.transitToSearch()
            }

            mainMediaButton.setOnClickListener {
                viewModel.transitToMedia()

            }

            mainSettingsButton.setOnClickListener {
                viewModel.transitToSettings()
            }

        }
    }

    private fun transitToActivity(state: MainEvent) {
        val targetIntent = when (state) {
            MainEvent.TransitionToMediaLibrary -> Intent(
                this@MainActivity,
                MediaLibraryActivity::class.java
            )

            MainEvent.TransitionToSearch -> Intent(this@MainActivity, SearchActivity::class.java)

            MainEvent.TransitionToSettings -> Intent(
                this@MainActivity,
                SettingsActivity::class.java
            )
        }
        startActivity(targetIntent)
    }

}