package com.example.dailyplanner.ui.schedule

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.Time
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dailyplanner.R
import com.example.dailyplanner.database.NODE_SCHEDULE
import com.example.dailyplanner.database.REF_DATABASE_ROOT
import com.example.dailyplanner.database.createAndPushScheduleToDatabase
import com.example.dailyplanner.database.updateScheduleDatabase
import com.example.dailyplanner.databinding.FragmentScheduleEditBinding
import com.example.dailyplanner.modules.CommonModel
import com.example.dailyplanner.utilities.APP_ACTIVITY
import com.example.dailyplanner.utilities.UID
import com.example.dailyplanner.utilities.showToast

class ScheduleEditFragment(
    private val scheduleCommonModel: CommonModel = CommonModel(),
    private val edit: Boolean,
    private val selectedDate: String
) : Fragment(R.layout.fragment_schedule_edit) {
    private var _binding: FragmentScheduleEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var popupWindowTypeLesson: PopupWindow
    private lateinit var popupViewType: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleEditBinding.inflate(inflater, container, false)
        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.GONE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.GONE
        popupViewType = inflater.inflate(R.layout.element_popup_schedule_type_lesson, null)
        initFields()
        saveSchedule()
        cancelSchedule()
        popupShowDropDown()
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

    private fun initFields() {
        binding.nameLessonSchedule.setText(scheduleCommonModel.name_lesson)
        binding.teachersNameSchedule.setText(scheduleCommonModel.teacher_name)
        binding.typeLessonsSchedule.text = scheduleCommonModel.type_lesson
        binding.corpusSchedule.setText(scheduleCommonModel.corpus)
        binding.lectureHall.setText(scheduleCommonModel.lecture_hall)
        if (edit) {
            binding.startTimeEdit.text = scheduleCommonModel.time_start
            binding.endTimeEdit.text = scheduleCommonModel.time_end
        } else {
            binding.startTimeEdit.text = "09:00"
            binding.endTimeEdit.text = "10:30"
        }
    }

    private fun initFunc() {
        popupWindows()
        popupTypeLessonDismiss()
        deleteDatabaseSchedule()
        timePickerTimeStartEnd()
    }

    @SuppressLint("SimpleDateFormat")
    private fun pushDatabaseSchedule() {
        val name_lesson =
            binding.nameLessonSchedule.text.toString()
        val teacher_name =
            binding.teachersNameSchedule.text.toString()
        val type_lesson =
            binding.typeLessonsSchedule.text.toString()
        val lecture_hall =
            binding.lectureHall.text.toString()
        val corpus =
            binding.corpusSchedule.text.toString()
        val timeStart =
            binding.startTimeEdit.text.toString()
        val timeEnd =
            binding.endTimeEdit.text.toString()
        if (edit) {
            updateScheduleDatabase(name_lesson, teacher_name, type_lesson, corpus, lecture_hall,
                timeStart, timeEnd, scheduleCommonModel.id, selectedDate) {
                getBackScreen()
            }
        } else {
            createAndPushScheduleToDatabase(name_lesson, teacher_name, type_lesson, corpus,
                lecture_hall, timeStart, timeEnd, selectedDate) {
                getBackScreen()
            }
        }
    }

    private fun saveSchedule() {
        binding.save.setOnClickListener() {
            val nameLessonSchedule =
                binding.nameLessonSchedule.text.toString()
            val teacherNameSchedule =
                binding.teachersNameSchedule.text.toString()
            val typeLessonsSchedule =
                binding.typeLessonsSchedule.text.toString()
            val corpusSchedule =
                binding.corpusSchedule.text.toString()
            val lectureHall =
                binding.lectureHall.text.toString()
            when {
                nameLessonSchedule.isEmpty() -> showToast("Введите название занятия")
                teacherNameSchedule.isEmpty() -> showToast("Введите имя преподавателя")
                typeLessonsSchedule.isEmpty() -> showToast("Выберите тип занятия")
                corpusSchedule.isEmpty() -> showToast("Введите номер корпуса")
                lectureHall.isEmpty() -> showToast("Введите номер аудитории")
                else -> pushDatabaseSchedule()
            }
        }
    }

    private fun cancelSchedule() {
        binding.cancel.setOnClickListener {
            getBackScreen()
        }
    }

    private fun deleteDatabaseSchedule() {
        if (edit) {
            binding.deleteSchedule.setOnClickListener() {
                REF_DATABASE_ROOT.child(NODE_SCHEDULE).child(UID).child(selectedDate)
                    .child(scheduleCommonModel.id)
                    .removeValue()
                    .addOnSuccessListener { getBackScreen() }
            }
        } else binding.deleteSchedule.visibility = View.GONE
    }

    private fun getBackScreen() {
        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.VISIBLE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.VISIBLE
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }

    //Инициализация всплывающего меню
    private fun popupWindows() {
        popupWindowTypeLesson = PopupWindow(
            popupViewType,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        popupWindowTypeLesson.elevation = 10f
        popupWindowTypeLesson.isOutsideTouchable = true
    }

    //Функция появления всплывающего меню
    private fun popupShowDropDown() {
        binding.typeLessonsSchedule.setOnClickListener {
            popupWindowTypeLesson.showAsDropDown(binding.typeLessonsSchedule)
            popupWindowTypeLesson.isFocusable = true;
            popupWindowTypeLesson.update()
        }
    }

    private fun popupTypeLessonDismiss() {
        popupViewType.findViewById<TextView>(R.id.practical_lesson_text).setOnClickListener {
            binding.typeLessonsSchedule.text = "ПЗ"
            popupWindowTypeLesson.dismiss()
        }
        popupViewType.findViewById<TextView>(R.id.lecture_session_text).setOnClickListener {
            binding.typeLessonsSchedule.text = "ЛК"
            popupWindowTypeLesson.dismiss()
        }
        popupViewType.findViewById<TextView>(R.id.laboratory_work_text).setOnClickListener {
            binding.typeLessonsSchedule.text = "ЛР"
            popupWindowTypeLesson.dismiss()
        }
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