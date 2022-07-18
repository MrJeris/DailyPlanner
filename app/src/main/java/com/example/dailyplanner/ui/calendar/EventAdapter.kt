package com.example.dailyplanner.ui.calendar

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyplanner.R
import com.example.dailyplanner.modules.CommonModel
import com.example.dailyplanner.ui.schedule.ScheduleEditFragment
import com.example.dailyplanner.utilities.replaceFragment

class EventAdapter(private var selectedDate: String) : RecyclerView.Adapter<EventAdapter.EventHolder>() {
    private var scheduleList = mutableListOf<CommonModel>()

    fun updateListSchedule(schedule: CommonModel) {
        scheduleList.add(schedule)
        notifyItemInserted(scheduleList.size)
    }

    class EventHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name_lesson: TextView = view.findViewById(R.id.element_name_lesson_schedule)
        val teacher_name: TextView = view.findViewById(R.id.element_teacher_name_schedule)
        val type_lesson: TextView = view.findViewById(R.id.element_type_lesson_schedule)
        val corpus_hall_schedule: TextView = view.findViewById(R.id.element_corpus_hall_schedule)
        val timeStartEnd: TextView = view.findViewById(R.id.element_schedule_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.element_schedule, parent, false)
        view.layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT
        return EventHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.name_lesson.text = scheduleList[position].name_lesson
        holder.teacher_name.text = scheduleList[position].teacher_name
        holder.type_lesson.text = scheduleList[position].type_lesson
        holder.corpus_hall_schedule.text =
            "${scheduleList[position].corpus} ‒ ${scheduleList[position].lecture_hall}"
        holder.timeStartEnd.text =
            "${scheduleList[position].time_start} ‒ ${scheduleList[position].time_end}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewAttachedToWindow(holder: EventHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            replaceFragment(
                ScheduleEditFragment(
                    scheduleList[holder.adapterPosition],
                    true,
                    selectedDate
                )
            )
        }
    }

    override fun onViewDetachedFromWindow(holder: EventHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount() = scheduleList.size
}