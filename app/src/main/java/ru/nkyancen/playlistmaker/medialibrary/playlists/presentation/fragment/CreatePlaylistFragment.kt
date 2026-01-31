package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.FragmentNewPlaylistBinding
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.NewPlaylistState
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.viewmodel.CreatePlaylistViewModel


class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val newPlaylistViewModel: CreatePlaylistViewModel by viewModel()

    private lateinit var exitDialog: MaterialAlertDialogBuilder

    private lateinit var pickPhoto: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)

        pickPhoto  = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            newPlaylistViewModel.setImageUri(uri)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exitDialog = MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(getString(R.string.new_playlist_exit_dialog_title))
            .setMessage(getString(R.string.new_playlist_exit_dialog_description))
            .setNeutralButton(getString(R.string.cansel)) { _, _ -> }
            .setPositiveButton(getString(R.string.complete)) { _, _ ->
                findNavController().navigateUp()
            }

        newPlaylistViewModel.observeNewPlaylistState().observe(viewLifecycleOwner) {
            render(it)
        }

        newPlaylistViewModel.observeShowMessage().observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_created, it),
                Toast.LENGTH_LONG
            ).show()
        }

        binding.newPlaylistHeader.setNavigationOnClickListener {
            exitFromFragment()
        }

        binding.newPlaylistCreateButton.setOnClickListener {
            newPlaylistViewModel.savePlaylist(
                binding.newPlaylistTitleEdt.text!!.toString(),
                binding.newPlaylistDescriptionEdt.text?.toString()
            )

            findNavController().navigateUp()
        }

        binding.newPlaylistTitleEdt.doOnTextChanged { s, _, _, _ ->
            newPlaylistViewModel.setCreateButtonEnable(s?.isNotBlank() ?: false)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitFromFragment()
                }
            }
        )

        binding.newPlaylistImage.setOnClickListener {
            pickPhoto.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun render(state: NewPlaylistState) {
        when (state) {
            is NewPlaylistState.Content -> showContent(state.isButtonEnable, state.imageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun exitFromFragment() {
        if (
            !binding.newPlaylistTitleEdt.text.isNullOrEmpty() ||
            !binding.newPlaylistDescriptionEdt.text.isNullOrEmpty() ||
            newPlaylistViewModel.isImageSet()
        ) {
            exitDialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showContent(isButtonEnable: Boolean, imageUri: Uri?) {
        binding.newPlaylistCreateButton.isEnabled = isButtonEnable
        if (imageUri == null) {
            binding.newPlaylistImage.setImageResource(R.drawable.ic_add_photo_312)
        } else {
            binding.newPlaylistImage.setImageURI(imageUri)
        }
    }
}