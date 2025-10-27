package ru.nkyancen.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.nkyancen.playlistmaker.databinding.ActivityMainBinding
import ru.nkyancen.playlistmaker.presentation.main.model.MainState
import ru.nkyancen.playlistmaker.presentation.main.viewmodel.MainViewModel
import ru.nkyancen.playlistmaker.ui.medialibrary.MediaLibraryActivity
import ru.nkyancen.playlistmaker.ui.search.SearchActivity
import ru.nkyancen.playlistmaker.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

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
                viewModel.setState(MainState.TransitionToSearch)
            }

            mainMediaButton.setOnClickListener {
                viewModel.setState(MainState.TransitionToMediaLibrary)

            }

            mainSettingsButton.setOnClickListener {
                viewModel.setState(MainState.TransitionToSettings)
            }

        }
    }

    private fun transitToActivity(state: MainState) {
        val targetIntent = when (state) {
            MainState.TransitionToMediaLibrary -> Intent(
                this@MainActivity,
                MediaLibraryActivity::class.java
            )

            MainState.TransitionToSearch -> Intent(this@MainActivity, SearchActivity::class.java)

            MainState.TransitionToSettings -> Intent(
                this@MainActivity,
                SettingsActivity::class.java
            )
        }
        startActivity(targetIntent)
    }

}