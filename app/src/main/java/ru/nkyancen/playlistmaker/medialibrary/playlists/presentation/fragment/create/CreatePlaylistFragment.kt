package ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.fragment.create

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
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.FragmentCreatePlaylistBinding
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.model.CreatePlaylistState
import ru.nkyancen.playlistmaker.medialibrary.playlists.presentation.viewmodel.CreatePlaylistViewModel

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreatePlaylistViewModel by viewModel()

    private lateinit var exitDialog: MaterialAlertDialogBuilder

    private lateinit var pickPhoto: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)

        pickPhoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.setImageUri(uri)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getLong(EDITABLE_PLAYLIST_ID_TAG) ?: 0L

        viewModel.setPlaylistId(playlistId)

        if (viewModel.isNewPlaylist()) {
            binding.createPlaylistCreateButton.text = getString(R.string.create_playlist)
            binding.createPlaylistHeader.title = getString(R.string.new_playlist)
        } else {
            binding.createPlaylistCreateButton.text = getString(R.string.editor_button_text)
            binding.createPlaylistHeader.title = getString(R.string.editor_title)

            viewModel.initializeContent()
        }

        exitDialog = MaterialAlertDialogBuilder(requireContext(), R.style.DialogTheme)
            .setTitle(getString(R.string.new_playlist_exit_dialog_title))
            .setMessage(getString(R.string.new_playlist_exit_dialog_description))
            .setNeutralButton(getString(R.string.cansel), null)
            .setPositiveButton(getString(R.string.complete)) { _, _ ->
                findNavController().navigateUp()
            }

        viewModel.observeNewPlaylistState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeShowMessage().observe(viewLifecycleOwner) {
            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_created, it),
                Toast.LENGTH_LONG
            ).show()
        }

        binding.createPlaylistHeader.setNavigationOnClickListener {
            exitFromFragment()
        }

        binding.createPlaylistCreateButton.setOnClickListener {
            viewModel.savePlaylist(
                binding.createPlaylistTitleEdt.text?.toString().orEmpty(),
                binding.createPlaylistDescriptionEdt.text?.toString()
            ) { _ ->
                findNavController().navigateUp()
            }
        }

        binding.createPlaylistTitleEdt.doOnTextChanged { s, _, _, _ ->
            viewModel.setCreateButtonEnable(s?.toString() ?: "")
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitFromFragment()
                }
            }
        )

        binding.createPlaylistImage.setOnClickListener {
            pickPhoto.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun render(state: CreatePlaylistState) {
        when (state) {
            is CreatePlaylistState.Content -> showContent(state.isButtonEnable, state.imageUri)
            is CreatePlaylistState.Init -> showInitContent(
                state.title,
                state.description,
                state.imageUri,
                state.isButtonEnable
            )
        }
    }

    private fun showInitContent(
        title: String,
        description: String,
        uri: Uri?,
        isButtonEnable: Boolean
    ) {
        showContent(isButtonEnable, uri)

        binding.createPlaylistTitleEdt.setText(title)
        binding.createPlaylistDescriptionEdt.setText(description)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun exitFromFragment() {
        if (!viewModel.isNewPlaylist() ||
            (binding.createPlaylistTitleEdt.text.isNullOrEmpty() &&
                    binding.createPlaylistDescriptionEdt.text.isNullOrEmpty() &&
                    !viewModel.isImageSet())
        ) {
            try {
                findNavController().navigateUp()
            } catch (_: Exception) {
            }

        } else {
            exitDialog.show()
        }
    }

    private fun showContent(isButtonEnable: Boolean, imageUri: Uri?) {
        binding.createPlaylistCreateButton.isEnabled = isButtonEnable
        if (imageUri == null) {
            binding.createPlaylistImage.setImageResource(R.drawable.ic_add_photo_312)
        } else {
            binding.createPlaylistImage.setImageURI(imageUri)
        }
    }

    companion object {
        private const val EDITABLE_PLAYLIST_ID_TAG = "Current PlaylistId"

        fun createArgs(playlistId: Long): Bundle =
            bundleOf(EDITABLE_PLAYLIST_ID_TAG to playlistId)
    }
}