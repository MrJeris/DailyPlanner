package com.example.dailyplanner.ui.calendar

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyplanner.R
import com.example.dailyplanner.utilities.APP_ACTIVITY
import java.time.LocalDate


class CalendarAdapter(
    private var days: ArrayList<LocalDate>,
    private var onItemListener: OnItemListener,
    private var calendarUtils: CalendarUtils
) : RecyclerView.Adapter<CalendarAdapter.CalendarHolder>() {

    class CalendarHolder(
        itemView: View,
        private val onItemListener: OnItemListener,
        private var days: ArrayList<LocalDate>
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        val dayOfMonth: TextView = itemView.findViewById(R.id.cellDayText)

        override fun onClick(view: View) {
            onItemListener.onItemClick(adapterPosition, days[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarHolder {
        val inflater =
            LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.element_calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        if (days.size > 15)
            layoutParams.height = (parent.height * 0.16667).toInt()
        else
            layoutParams.height = (parent.height)
        return CalendarHolder(view, onItemListener, days)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CalendarHolder, position: Int) {
        val date: LocalDate = days[position]
        if (date == LocalDate.MIN)
            holder.dayOfMonth.text = ""
        else {
            holder.dayOfMonth.text = date.dayOfMonth.toString()
            if (date == calendarUtils.selectedDate) {
                holder.dayOfMonth.setBackgroundResource(R.drawable.circle_white_solid_background)
                holder.dayOfMonth.setTextColor(Color.BLACK)
            }
            if (date.month != calendarUtils.selectedDate.month) {
                holder.dayOfMonth.setTextColor(ContextCompat.getColor(APP_ACTIVITY, R.color.bright_gray))
            }
        }
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate)
    }
}