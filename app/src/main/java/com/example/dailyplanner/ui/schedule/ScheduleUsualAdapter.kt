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

class ScheduleUsualAdapter : RecyclerView.Adapter<ScheduleUsualAdapter.ScheduleUsualHolder>() {

    private var scheduleUsualList = mutableListOf<CommonModel>()

    fun updateListSchedule(schedule: CommonModel) {
        scheduleUsualList.add(schedule)
        notifyItemInserted(scheduleUsualList.size)
    }

    class ScheduleUsualHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name_schedule: TextView = view.findViewById(R.id.name_usual_schedule)
        val tasks_schedule: TextView = view.findViewById(R.id.tasks_schedule)
        val timeStartEnd: TextView = view.findViewById(R.id.element_schedule_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleUsualHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.element_schedule_usual, parent, false)

        view.layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT

        return ScheduleUsualHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScheduleUsualHolder, position: Int) {
        holder.name_schedule.text = scheduleUsualList[position].name_schedule
        holder.tasks_schedule.text = scheduleUsualList[position].tasks_day_schedule
        holder.timeStartEnd.text =
            "${scheduleUsualList[position].time_start} ‒ ${scheduleUsualList[position].time_end}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewAttachedToWindow(holder: ScheduleUsualHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            replaceFragment(
                ScheduleUsualEditFragment(
                    scheduleUsualList[holder.adapterPosition],
                    true,
                    LocalDate.now().toString()
                )
            )
        }
    }

    override fun onViewDetachedFromWindow(holder: ScheduleUsualHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount() = scheduleUsualList.size
}



