package com.example.dailyplanner.ui.notes

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyplanner.R
import com.example.dailyplanner.modules.CommonModel
import com.example.dailyplanner.utilities.APP_ACTIVITY
import com.example.dailyplanner.utilities.replaceFragment

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NotesHolder>() {

    private var noteList = mutableListOf<CommonModel>()

    fun updateListNote(note: CommonModel) {
        noteList.add(note)
        notifyItemInserted(noteList.size)
    }

    class NotesHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title_notes)
        val text: TextView = view.findViewById(R.id.notes_text)
        val date: TextView = view.findViewById(R.id.notes_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.element_notes, parent, false)

        val displayMetrics: DisplayMetrics = DisplayMetrics()
        APP_ACTIVITY.windowManager.defaultDisplay.getMetrics(displayMetrics)

        val deviceWidth: Int = displayMetrics.widthPixels / 3
        view.layoutParams.width = deviceWidth

        val deviceHeight: Int = displayMetrics.heightPixels / 3 - 50
        view.layoutParams.height = deviceHeight

        return NotesHolder(view)
    }

    override fun onBindViewHolder(holder: NotesHolder, position: Int) {
        holder.title.text = noteList[position].title
        holder.text.text = noteList[position].text
        holder.date.text = noteList[position].date
    }

    override fun onViewAttachedToWindow(holder: NotesHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            replaceFragment(NotesEditFragment(noteList[holder.adapterPosition], true))
        }
    }

    override fun onViewDetachedFromWindow(holder: NotesHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }
    override fun getItemCount() = noteList.size
}