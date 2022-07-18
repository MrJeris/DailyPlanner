package com.example.dailyplanner.ui.schedule

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.Time
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dailyplanner.R
import com.example.dailyplanner.database.NODE_SCHEDULE_USUAL
import com.example.dailyplanner.database.REF_DATABASE_ROOT
import com.example.dailyplanner.database.createAndPushScheduleUsualToDatabase
import com.example.dailyplanner.database.updateScheduleUsualDatabase
import com.example.dailyplanner.databinding.FragmentScheduleUsualEditBinding
import com.example.dailyplanner.modules.CommonModel
import com.example.dailyplanner.utilities.APP_ACTIVITY
import com.example.dailyplanner.utilities.UID
import com.example.dailyplanner.utilities.showToast

class ScheduleUsualEditFragment(
    private val scheduleUsualCommonModel: CommonModel = CommonModel(),
    private val edit: Boolean,
    private val selectedDate: String
) : Fragment(R.layout.fragment_schedule_usual_edit) {
    private var _binding: FragmentScheduleUsualEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleUsualEditBinding.inflate(inflater, container, false)

        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.GONE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.GONE
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
        clickSaveAndCancelButton()
        initValueFields()
        deleteDatabaseScheduleUsual()
        timePickerTimeStartEnd()
    }

    private fun initValueFields() {
        binding.nameSchedule.setText(scheduleUsualCommonModel.name_schedule)
        binding.tasksDaySchedule.setText(scheduleUsualCommonModel.tasks_day_schedule)
        binding.startTimeEdit.text = scheduleUsualCommonModel.time_start
        binding.endTimeEdit.text = scheduleUsualCommonModel.time_end

        if (edit) {
            binding.startTimeEdit.text = scheduleUsualCommonModel.time_start
            binding.endTimeEdit.text = scheduleUsualCommonModel.time_end
        } else {
            binding.startTimeEdit.text = "09:00"
            binding.endTimeEdit.text = "10:30"
        }
    }

    private fun clickSaveAndCancelButton() {
        binding.save.setOnClickListener() {
            val nameSchedule =
                binding.nameSchedule.text.toString()
            val tasksDaySchedule =
                binding.tasksDaySchedule.text.toString()
            when {
                nameSchedule.isEmpty() -> showToast("Введите название заметки")
                tasksDaySchedule.isEmpty() -> showToast("Введите цель на день")
                else -> pushDatabaseScheduleUsual()
            }
        }

        binding.cancel.setOnClickListener { getBackScreen() }
    }

    @SuppressLint("SimpleDateFormat")
    private fun pushDatabaseScheduleUsual() {
        val name_schedule =
            binding.nameSchedule.text.toString()
        val tasks_day_schedule =
            binding.tasksDaySchedule.text.toString()
        val timeStart =
            binding.startTimeEdit.text.toString()
        val timeEnd =
            binding.endTimeEdit.text.toString()

        if (edit) {
            updateScheduleUsualDatabase(
                name_schedule,
                tasks_day_schedule,
                timeStart,
                timeEnd,
                scheduleUsualCommonModel.id,
                selectedDate
            ) {
                getBackScreen()
            }
        } else {
            createAndPushScheduleUsualToDatabase(
                name_schedule,
                tasks_day_schedule,
                timeStart,
                timeEnd,
                selectedDate
            ) {
                getBackScreen()
            }
        }
    }

    private fun deleteDatabaseScheduleUsual() {
        if (edit) {
            binding.deleteScheduleUsual.setOnClickListener() {
                REF_DATABASE_ROOT.child(NODE_SCHEDULE_USUAL).child(UID).child(selectedDate)
                    .child(scheduleUsualCommonModel.id)
                    .removeValue()
                    .addOnSuccessListener { getBackScreen() }
            }
        } else binding.deleteScheduleUsual.visibility = View.GONE
    }

    private fun getBackScreen() {
        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.VISIBLE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.VISIBLE
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }

    private fun timePickerTimeStartEnd() {
        binding.startTimeEdit.setOnClickListener {
            TimePickerDialog(
                APP_ACTIVITY,
                { _, hourOfDay, minute ->
                    if (hourOfDay < 10 && minute < 10)
                        binding.startTimeEdit.text =
                            "0$hourOfDay:0$minute"
                    else if (hourOfDay < 10 && minute >= 10)
                        binding.startTimeEdit.text =
                            "0$hourOfDay:$minute"
                    else if (hourOfDay >= 10 && minute < 10)
                        binding.startTimeEdit.text =
                            "$hourOfDay:0$minute"
                    else if (hourOfDay >= 10 && minute >= 10)
                        binding.startTimeEdit.text =
                            "$hourOfDay:$minute"
                }, Time.HOUR, Time.MINUTE, true
            ).show()
        }
        binding.endTimeEdit.setOnClickListener {
            TimePickerDialog(
                APP_ACTIVITY,
                { _, hourOfDay, minute ->
                    if (hourOfDay < 10 && minute < 10)
                        binding.endTimeEdit.text =
                            "0$hourOfDay:0$minute"
                    else if (hourOfDay < 10 && minute >= 10)
                        binding.endTimeEdit.text =
                            "0$hourOfDay:$minute"
                    else if (hourOfDay >= 10 && minute < 10)
                        binding.endTimeEdit.text =
                            "$hourOfDay:0$minute"
                    else if (hourOfDay >= 10 && minute >= 10)
                        binding.endTimeEdit.text =
                            "$hourOfDay:$minute"
                }, Time.HOUR, Time.MINUTE, true
            ).show()
        }
    }
}
