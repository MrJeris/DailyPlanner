package com.example.dailyplanner.ui.calendar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyplanner.R
import com.example.dailyplanner.database.*
import com.example.dailyplanner.databinding.FragmentCalendarBinding
import com.example.dailyplanner.modules.CommonModel
import com.example.dailyplanner.ui.schedule.ScheduleEditFragment
import com.example.dailyplanner.ui.schedule.ScheduleUsualEditFragment
import com.example.dailyplanner.utilities.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class CalendarFragment : Fragment(R.layout.fragment_calendar), CalendarAdapter.OnItemListener {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val calendarUtils = CalendarUtils()
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventUsualAdapter: EventUsualAdapter
    private var scheduleList = listOf<CommonModel>()
    private val scheduleCommonModel: CommonModel = CommonModel()
    private var scheduleUsualList = listOf<CommonModel>()
    private val scheduleUsualCommonModel: CommonModel = CommonModel()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        calendarUtils.selectedDate = LocalDate.now()
        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.VISIBLE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.VISIBLE
        initFunc()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initFunc() {
        initAdapterEvent()
        setWeekView()
        transitionNextMouthWeek()
        transitionPrevMouthWeek()
        changingCalendar()
        enteringDate()
        newEvent()
    }

    //Инициализация недельного и месячного представления календаря
    @RequiresApi(Build.VERSION_CODES.O)
    fun initAdapterEvent() {
        getDataSchedules(calendarUtils.selectedDate.toString())
        eventAdapter = EventAdapter(calendarUtils.selectedDate.toString())
        binding.eventList.layoutManager = LinearLayoutManager(APP_ACTIVITY)
        binding.eventList.adapter = eventAdapter

        getDataSchedulesUsual(calendarUtils.selectedDate.toString())
        eventUsualAdapter = EventUsualAdapter(calendarUtils.selectedDate.toString())
        binding.eventUsualList.layoutManager = LinearLayoutManager(APP_ACTIVITY)
        binding.eventUsualList.adapter = eventUsualAdapter
    }

    //Отображение месячного представления календаря
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {
        binding.dayMouthYearDayWeek.text =
            calendarUtils.monthYearFromDate(calendarUtils.selectedDate)
        val daysInMonth = calendarUtils.daysInMonthArray(calendarUtils.selectedDate)
        val calendarAdapter = CalendarAdapter(daysInMonth, this, calendarUtils)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(APP_ACTIVITY, 7)
        binding.calendarRecyclerView.layoutManager = layoutManager
        binding.calendarRecyclerView.adapter = calendarAdapter
    }

    //Отображение недельного представления календаря
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setWeekView() {
        binding.dayMouthYearDayWeek.text =
            calendarUtils.monthYearFromDate(calendarUtils.selectedDate)
        val days = calendarUtils.daysInWeekArray(calendarUtils.selectedDate)
        val calendarAdapter = CalendarAdapter(days, this, calendarUtils)
        val layoutManager: RecyclerView.LayoutManager =
            GridLayoutManager(APP_ACTIVITY, 7)
        binding.calendarWeekRecyclerView.layoutManager = layoutManager
        binding.calendarWeekRecyclerView.adapter = calendarAdapter
    }

    //Выбор дня при нажатии
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int, date: LocalDate) {
        if (binding.calendarWeekRecyclerView.visibility == View.VISIBLE) {
            calendarUtils.selectedDate = date
            setWeekView()
        } else {
            calendarUtils.selectedDate = date
            setMonthView()
        }
        initAdapterEvent()
    }

    //Смена вида календаря
    @RequiresApi(Build.VERSION_CODES.O)
    private fun transitionNextMouthWeek() {
        //Следующий месяц/неделя
        binding.nextMonth.setOnClickListener {
            if (binding.calendarWeekRecyclerView.visibility == View.VISIBLE) {
                calendarUtils.selectedDate = calendarUtils.selectedDate.plusWeeks(1)
                setWeekView()
                initAdapterEvent()
            } else {
                calendarUtils.selectedDate = calendarUtils.selectedDate.plusMonths(1)
                setMonthView()
                initAdapterEvent()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun transitionPrevMouthWeek() {
        //Предыдущий месяц/неделя
        binding.prevMonth.setOnClickListener {
            if (binding.calendarWeekRecyclerView.visibility == View.VISIBLE) {
                calendarUtils.selectedDate = calendarUtils.selectedDate.minusWeeks(1)
                setWeekView()
                initAdapterEvent()
            } else {
                calendarUtils.selectedDate = calendarUtils.selectedDate.minusMonths(1)
                setMonthView()
                initAdapterEvent()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun changingCalendar() {
        //Смена календаря
        binding.changingCalendar.setOnClickListener {
            if (binding.calendarWeekRecyclerView.visibility == View.VISIBLE) {
                binding.calendarWeekRecyclerView.visibility = View.GONE
                binding.calendarRecyclerView.visibility = View.VISIBLE
                binding.downArrowImg.rotation = 90f
                setMonthView()
            } else {
                binding.calendarWeekRecyclerView.visibility = View.VISIBLE
                binding.calendarRecyclerView.visibility = View.GONE
                binding.downArrowImg.rotation = -90f
                setWeekView()
            }
        }
    }

    //Ввод даты вручную
    @RequiresApi(Build.VERSION_CODES.O)
    private fun enteringDate() {
        binding.enterMonth.setOnClickListener {
            if (Regex("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d")
                    .matches(binding.editDate.text)
            ) {
                binding.editDate.clearFocus()
                val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val date: LocalDate = LocalDate.parse(binding.editDate.text, formatter)
                calendarUtils.selectedDate = date
                if (binding.calendarWeekRecyclerView.visibility == View.VISIBLE) {
                    setWeekView()
                } else {
                    setMonthView()
                }
                binding.editDate.text.clear()
                initAdapterEvent()
            } else showToast("Неправильный формат даты")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun getDataSchedules(selectedDate: String) {
        REF_DATABASE_ROOT.child(NODE_SCHEDULE).child(UID).child(selectedDate).orderByChild(CHILD_TIME_START)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
                scheduleList = dataSnapshot.children.map {
                    it.getCommonModel()
                }
                scheduleList.forEach { model ->
                    REF_DATABASE_ROOT.child(NODE_SCHEDULE).child(UID).child(selectedDate).child(model.id)
                        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                            val schedule = dataSnapshot1.getCommonModel()
                            eventAdapter.updateListSchedule(schedule)
                        })
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun getDataSchedulesUsual(selectedDate: String) {
        REF_DATABASE_ROOT.child(NODE_SCHEDULE_USUAL).child(UID).child(selectedDate).orderByChild(CHILD_TIME_START)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
                scheduleUsualList = dataSnapshot.children.map {
                    it.getCommonModel()
                }
                scheduleUsualList.forEach { model ->
                    REF_DATABASE_ROOT.child(NODE_SCHEDULE_USUAL).child(UID).child(selectedDate).child(model.id)
                        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                            val schedule = dataSnapshot1.getCommonModel()
                            eventUsualAdapter.updateListScheduleUsual(schedule)
                        })
                }
            })
    }

    private fun newEvent() {
        APP_ACTIVITY.binding.fab.setOnClickListener {
            val textTitle = TextView(APP_ACTIVITY)
            textTitle.text = "Выберите тип расписания"
            textTitle.gravity = Gravity.CENTER
            textTitle.textSize = 22f
            textTitle.setPadding(20, 20, 20, 20)
            textTitle.setTextColor(APP_ACTIVITY.getColor(R.color.bright_gray))
            val builder = AlertDialog.Builder(
                ContextThemeWrapper(APP_ACTIVITY, R.style.AlertDialogCustom))
            builder.setCustomTitle(textTitle)
            builder.setPositiveButton("Обычное") { _, _ ->
                replaceFragment(ScheduleUsualEditFragment(scheduleUsualCommonModel,
                    false, calendarUtils.selectedDate.toString()))
            }
            builder.setNegativeButton("Учебное") { _, _ ->
                replaceFragment(ScheduleEditFragment(scheduleCommonModel,
                    false, calendarUtils.selectedDate.toString()))
            }
            builder.show()
        }
    }
}
