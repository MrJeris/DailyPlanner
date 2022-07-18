package com.example.dailyplanner.ui.calendar

import android.annotation.SuppressLint
import com.example.dailyplanner.ui.schedule.ScheduleUsualEditFragment

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

class EventUsualAdapter(var selectedDate: String) :
    RecyclerView.Adapter<EventUsualAdapter.EventUsualHolder>() {

    private var scheduleUsualList = mutableListOf<CommonModel>()

    fun updateListScheduleUsual(schedule: CommonModel) {
        scheduleUsualList.add(schedule)
        notifyItemInserted(scheduleUsualList.size)
    }

    class EventUsualHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name_schedule: TextView = view.findViewById(R.id.name_usual_schedule)
        val tasks_schedule: TextView = view.findViewById(R.id.tasks_schedule)
        val timeStartEnd: TextView = view.findViewById(R.id.element_schedule_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventUsualHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.element_schedule_usual, parent, false)

        view.layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT

        return EventUsualHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EventUsualHolder, position: Int) {
        holder.name_schedule.text = scheduleUsualList[position].name_schedule
        holder.tasks_schedule.text = scheduleUsualList[position].tasks_day_schedule
        holder.timeStartEnd.text =
            "${scheduleUsualList[position].time_start} ‒ ${scheduleUsualList[position].time_end}"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewAttachedToWindow(holder: EventUsualHolder) {
        super.onViewAttachedToWindow(holder)
        holder.itemView.setOnClickListener {
            replaceFragment(
                ScheduleUsualEditFragment(
                    scheduleUsualList[holder.adapterPosition],
                    true,
                    selectedDate
                )
            )
        }
    }

    override fun onViewDetachedFromWindow(holder: EventUsualHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.setOnClickListener(null)
    }

    override fun getItemCount() = scheduleUsualList.size
}