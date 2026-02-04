package ru.nkyancen.playlistmaker.playlist_detail.presentation.fragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.android.ext.android.getKoin
import org.koin.core.parameter.parametersOf
import ru.nkyancen.playlistmaker.R
import ru.nkyancen.playlistmaker.databinding.FragmentEditorPlaylistBinding
import ru.nkyancen.playlistmaker.playlist_detail.presentation.model.PlaylistEditorState
import ru.nkyancen.playlistmaker.playlist_detail.presentation.viewmodel.PlaylistEditorViewModel

class PlaylistEditorFragment : Fragment() {
    private var _binding: FragmentEditorPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlaylistEditorViewModel

    private lateinit var pickPhoto: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorPlaylistBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editablePlaylistId = arguments?.getLong(EDITABLE_PLAYLIST_ID_TAG)

        viewModel = getKoin().get() {
            parametersOf(editablePlaylistId)
        }

        viewModel.observeEditorState().observe(viewLifecycleOwner) {
            render(it)
        }

        pickPhoto  = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            viewModel.setImageUri(uri)
        }

        binding.editorPlaylistTitleEdt.setText(viewModel.getCurrentTitle())
        binding.editorPlaylistDescriptionEdt.setText(viewModel.getCurrentDescription())

        binding.editorPlaylistHeader.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.editorPlaylistSaveButton.setOnClickListener {
            viewModel.saveChanges(
                binding.editorPlaylistTitleEdt.text!!.toString(),
                binding.editorPlaylistDescriptionEdt.text?.toString()
            )

            findNavController().navigateUp()
        }

        binding.editorPlaylistTitleEdt.doOnTextChanged { s, _, _, _ ->
            viewModel.setButtonEnable(s?.toString() ?: "")
        }

        binding.editorPlaylistCover.setOnClickListener {
            pickPhoto.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun render(state: PlaylistEditorState) {
        when (state) {
            is PlaylistEditorState.Content -> showContent(state.isButtonEnable, state.uri)
        }
    }

    private fun showContent(isButtonEnable: Boolean, uri: Uri?) {
        binding.apply {
            if (uri == null) {
                editorPlaylistCover.setImageResource(R.drawable.ic_add_photo_312)
            } else {
                editorPlaylistCover.setImageURI(uri)
            }

            editorPlaylistSaveButton.isEnabled = isButtonEnable
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        private const val EDITABLE_PLAYLIST_ID_TAG = "Current PlaylistId"

        fun createArgs(playlistId: Long): Bundle =
            bundleOf(EDITABLE_PLAYLIST_ID_TAG to playlistId)
    }
}