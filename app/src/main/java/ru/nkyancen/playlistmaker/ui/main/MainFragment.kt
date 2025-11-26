package ru.nkyancen.playlistmaker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import ru.nkyancen.playlistmaker.databinding.FragmentMainBinding
import ru.nkyancen.playlistmaker.ui.medialibrary.MediaLibraryFragment
import ru.nkyancen.playlistmaker.ui.search.SearchFragment
import ru.nkyancen.playlistmaker.ui.settings.SettingsFragment

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.apply {
            mainSettingsButton.setOnClickListener {
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(container!!.id, SettingsFragment())
                    addToBackStack("main_fragment")
                }
            }

            mainMediaButton.setOnClickListener {
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(container!!.id, MediaLibraryFragment())
                    addToBackStack("main_fragment")
                }
            }

            mainSearchButton.setOnClickListener {
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(container!!.id, SearchFragment())
                    addToBackStack("main_fragment")
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}