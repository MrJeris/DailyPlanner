package com.example.dailyplanner.ui.schedule
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
import com.example.dailyplanner.utilities.replaceFragment
import java.time.LocalDate

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder>() {
    private var scheduleList = mutableListOf<CommonModel>()

    fun updateListSchedule(schedule: CommonModel) {
        scheduleList.add(schedule)
        notifyItemInserted(scheduleList.size)
    }

    class ScheduleHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name_lesson: TextView = view.findViewById(R.id.element_name_lesson_schedule)
        val teacher_name: TextView = view.findViewById(R.id.element_teacher_name_schedule)
        val type_lesson: TextView = view.findViewById(R.id.element_type_lesson_schedule)
        val corpus_hall_schedule: TextView = view.findViewById(R.id.element_corpus_hall_schedule)
        val timeStartEnd: TextView = view.findViewById(R.id.element_schedule_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.element_schedule, parent, false)
        view.layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT
        return ScheduleHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScheduleHolder, position: Int) {
        holder.name_lesson.text = scheduleList[position].name_lesson
        holder.teacher_name.text = scheduleList[position].teacher_name
        holder.type_lesson.text = scheduleList[position].type_lesson
        holder.corpus_hall_schedule.text =
            "${scheduleList[position].corpus} ‒ ${scheduleList[position].lecture_hall}"
        holder.timeStartEnd.text =
            "${scheduleList[position].time_start} ‒ ${scheduleList[position].time_end}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewAttachedToWindow(holder: ScheduleHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            replaceFragment(
                ScheduleEditFragment(
                    scheduleList[holder.adapterPosition],
                    true,
                    LocalDate.now().toString()))
        }
    }

    override fun onViewDetachedFromWindow(holder: ScheduleHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount() = scheduleList.size
}