package com.example.notemakingapp.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notemakingapp.MainActivity
import com.example.notemakingapp.R
import com.example.notemakingapp.databinding.FragmentUpdateNoteBinding
import com.example.notemakingapp.model.Note
import com.example.notemakingapp.noteViewModel.NoteViewModel

class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {

    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var currentNote: Note
    private val args: UpdateNoteFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        notesViewModel = (activity as MainActivity).noteViewModel

        // Retrieve the note passed as an argument
        currentNote = args.note ?: run {
            Toast.makeText(requireContext(), "Error loading note", Toast.LENGTH_SHORT).show()
            view.findNavController().navigateUp()
            return
        }

        // Bind the note data to the views
        binding.etNoteTitleUpdate.setText(currentNote.noteTitle)
        binding.etNoteBodyUpdate.setText(currentNote.noteBody)

        // Handle save button click
        binding.fabDone.setOnClickListener {
            saveUpdatedNote()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun saveUpdatedNote() {
        val title = binding.etNoteTitleUpdate.text.toString().trim()
        val body = binding.etNoteBodyUpdate.text.toString().trim()

        if (title.isNotEmpty()) {
            val updatedNote = Note(
                id = currentNote.id,
                noteTitle = title,
                noteBody = body
            )
            notesViewModel.updateNote(updatedNote)
            Toast.makeText(requireContext(), "Note Updated Successfully", Toast.LENGTH_LONG).show()
            view?.findNavController()?.navigate(R.id.action_updateNoteFragment_to_homeFragment)
        } else {
            Toast.makeText(requireContext(), "Please enter a note title", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to delete this note?")
            setPositiveButton("Delete") { _, _ ->
                notesViewModel.deleteNote(currentNote)
                view?.findNavController()?.navigate(R.id.action_updateNoteFragment_to_homeFragment)
            }
            setNegativeButton("Cancel", null)
        }.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_delete, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                deleteNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
