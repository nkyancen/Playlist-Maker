package ru.nkyancen.playlistmaker.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val paddingBottom = if (binding.rootBottomNavMenu.isVisible) {
                0
            } else {
                systemBars.bottom
            }
            v.setPadding(systemBars.left, systemBars.top, systemBars.left, paddingBottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController

        binding.rootBottomNavMenu.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.mediaPlayerFragment ->
                    binding.rootBottomNavMenu.visibility = View.GONE
                else -> binding.rootBottomNavMenu.visibility = View.VISIBLE
            }
        }
    }
}