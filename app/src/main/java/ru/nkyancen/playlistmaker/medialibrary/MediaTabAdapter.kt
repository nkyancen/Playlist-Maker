package ru.nkyancen.playlistmaker.medialibrary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.nkyancen.playlistmaker.medialibrary.favorites.presentation.fragment.FavoritesFragment
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.fragment.PlaylistsFragment

class MediaTabAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment =
        when (position) {
            0 -> FavoritesFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }

    override fun getItemCount(): Int = 2
}