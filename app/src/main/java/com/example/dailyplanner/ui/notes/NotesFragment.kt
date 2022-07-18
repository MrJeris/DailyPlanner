package com.example.dailyplanner.ui.notes
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dailyplanner.R
import com.example.dailyplanner.database.CHILD_DATE
import com.example.dailyplanner.database.NODE_NOTES
import com.example.dailyplanner.database.REF_DATABASE_ROOT
import com.example.dailyplanner.database.getCommonModel
import com.example.dailyplanner.databinding.FragmentNotesBinding
import com.example.dailyplanner.modules.CommonModel
import com.example.dailyplanner.utilities.APP_ACTIVITY
import com.example.dailyplanner.utilities.AppValueEventListener
import com.example.dailyplanner.utilities.UID
import com.example.dailyplanner.utilities.replaceFragment

class NotesFragment : Fragment(R.layout.fragment_notes) {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesAdapter: NotesAdapter

    private var noteList = listOf<CommonModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.VISIBLE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.VISIBLE
        addNote()
        getDataNotes()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        initFunc()
    }

    private fun initFunc() {
        notesAdapter = NotesAdapter()
        binding.recyclerViewNotes.apply {
            layoutManager = GridLayoutManager(context, 3)
            this.adapter = notesAdapter
        }
    }

    private fun addNote() {
        APP_ACTIVITY.binding.fab.setOnClickListener {
            replaceFragment(NotesEditFragment(CommonModel(), false))
        }
    }

    private fun getDataNotes() {
        REF_DATABASE_ROOT.child(NODE_NOTES).child(UID).orderByChild(CHILD_DATE)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
                noteList = dataSnapshot.children.map {
                    it.getCommonModel()
                }
                noteList.forEach { model ->
                    REF_DATABASE_ROOT.child(NODE_NOTES).child(UID).child(model.id)
                        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                            val note = dataSnapshot1.getCommonModel()
                            notesAdapter.updateListNote(note)
                        })
                }
            })
    }
}