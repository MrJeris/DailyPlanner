package com.example.dailyplanner.ui.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dailyplanner.R
import com.example.dailyplanner.database.NODE_NOTES
import com.example.dailyplanner.database.REF_DATABASE_ROOT
import com.example.dailyplanner.database.createAndPushNoteToDatabase
import com.example.dailyplanner.database.updateNoteDatabase
import com.example.dailyplanner.databinding.FragmentNotesEditBinding
import com.example.dailyplanner.modules.CommonModel
import com.example.dailyplanner.utilities.APP_ACTIVITY
import com.example.dailyplanner.utilities.UID
import com.example.dailyplanner.utilities.showToast
import java.text.SimpleDateFormat
import java.util.*

class NotesEditFragment(
    private val noteCommonModel: CommonModel = CommonModel(),
    private val edit: Boolean
) : Fragment(R.layout.fragment_notes_edit) {
    private var _binding: FragmentNotesEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesEditBinding.inflate(inflater, container, false)

        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.GONE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.GONE

        initFields()
        saveNote()
        cancelNote()
        deleteDatabaseNote()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveNote() {
        binding.saveNote.setOnClickListener() {
            val titleNote =
                binding.editTitleNote.text.toString()
            val textNote =
                binding.editTextNote.text.toString()
            when {
                titleNote.isEmpty() -> showToast("Введите название заметки")
                textNote.isEmpty() -> showToast("Введите текст заметки")
                else -> pushDatabaseNote()
            }
        }
    }

    private fun initFields() {
        binding.editTitleNote.setText(noteCommonModel.title)
        binding.editTextNote.setText(noteCommonModel.text)
    }

    private fun cancelNote() {
        binding.cancelNote.setOnClickListener {
            getBackScreen()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun pushDatabaseNote() {
        val title = binding.editTitleNote.text.toString()
        val text = binding.editTextNote.text.toString()
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val date = sdf.format(Date())

        if (edit) {
            updateNoteDatabase(
                title,
                text,
                date,
                noteCommonModel.id
            ) {
                getBackScreen()
            }
        } else {
            createAndPushNoteToDatabase(
                title,
                text,
                date
            ) {
                getBackScreen()
            }
        }
    }

    private fun deleteDatabaseNote() {
        if (edit) {
            binding.deleteNote.setOnClickListener() {
                REF_DATABASE_ROOT.child(NODE_NOTES).child(UID).child(noteCommonModel.id)
                    .removeValue()
                    .addOnSuccessListener { getBackScreen() }
            }
        } else binding.deleteNote.visibility = View.GONE
    }

    private fun getBackScreen() {
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }
}