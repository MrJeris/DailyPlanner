package com.example.dailyplanner.ui.schedule

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dailyplanner.R
import com.example.dailyplanner.database.CHILD_TIME_START
import com.example.dailyplanner.database.NODE_SCHEDULE_USUAL
import com.example.dailyplanner.database.REF_DATABASE_ROOT
import com.example.dailyplanner.database.getCommonModel
import com.example.dailyplanner.databinding.FragmentScheduleUsualBinding
import com.example.dailyplanner.modules.CommonModel
import com.example.dailyplanner.utilities.APP_ACTIVITY
import com.example.dailyplanner.utilities.AppValueEventListener
import com.example.dailyplanner.utilities.UID
import com.example.dailyplanner.utilities.replaceFragment
import java.time.LocalDate


class ScheduleUsualFragment : Fragment(R.layout.fragment_schedule_usual) {
    private var _binding: FragmentScheduleUsualBinding? = null
    private val binding get() = _binding!!

    private lateinit var scheduleUsualAdapter: ScheduleUsualAdapter

    private var scheduleUsualList = listOf<CommonModel>()
    private val scheduleUsualCommonModel: CommonModel = CommonModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleUsualBinding.inflate(inflater, container, false)

        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.VISIBLE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.VISIBLE

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        initFields()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initFields() {
        scheduleUsualAdapter = ScheduleUsualAdapter()
        getDataSchedulesUsual()
        addScheduleUsual()
        binding.recyclerViewScheduleUsual.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = scheduleUsualAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addScheduleUsual() {
        //Нажатие на кнопку добавить +
        APP_ACTIVITY.binding.fab.setOnClickListener {
            replaceFragment(
                ScheduleUsualEditFragment(
                    scheduleUsualCommonModel,
                    false,
                    LocalDate.now().toString()
                )
            )
        }
    }

    //Получение из базы данных и передача данных в поля приложения
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun getDataSchedulesUsual() {
        REF_DATABASE_ROOT.child(NODE_SCHEDULE_USUAL).child(UID).child(LocalDate.now().toString())
            .orderByChild(CHILD_TIME_START)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
                scheduleUsualList = dataSnapshot.children.map {
                    it.getCommonModel()
                }
                scheduleUsualList.forEach { model ->
                    REF_DATABASE_ROOT.child(NODE_SCHEDULE_USUAL).child(UID)
                        .child(LocalDate.now().toString()).child(model.id)
                        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                            val schedule = dataSnapshot1.getCommonModel()
                            scheduleUsualAdapter.updateListSchedule(schedule)
                        })
                }
            })
    }
}