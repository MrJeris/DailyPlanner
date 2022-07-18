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
import com.example.dailyplanner.database.NODE_SCHEDULE
import com.example.dailyplanner.database.REF_DATABASE_ROOT
import com.example.dailyplanner.database.getCommonModel
import com.example.dailyplanner.databinding.FragmentScheduleBinding
import com.example.dailyplanner.modules.CommonModel
import com.example.dailyplanner.utilities.APP_ACTIVITY
import com.example.dailyplanner.utilities.AppValueEventListener
import com.example.dailyplanner.utilities.UID
import com.example.dailyplanner.utilities.replaceFragment
import java.time.LocalDate

class ScheduleFragment : Fragment(R.layout.fragment_schedule) {
    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!
    private lateinit var scheduleAdapter: ScheduleAdapter
    private var scheduleList = listOf<CommonModel>()
    private val scheduleCommonModel: CommonModel = CommonModel()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.VISIBLE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.VISIBLE
        getDataSchedules()
        addSchedule()
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

    //Инициализация расписания
    @RequiresApi(Build.VERSION_CODES.O)
    private fun initFields() {
        scheduleAdapter = ScheduleAdapter()
        binding.recyclerViewSchedule.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = scheduleAdapter
        }
    }

    //Нажатие на кнопку добавить +
    @RequiresApi(Build.VERSION_CODES.O)
    private fun addSchedule() {
        APP_ACTIVITY.binding.fab.setOnClickListener {
            replaceFragment(
                ScheduleEditFragment(
                    scheduleCommonModel,
                    false,
                    LocalDate.now().toString()
                )
            )
        }
    }

    //Получение из базы данных и передача данных в поля приложения
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun getDataSchedules() {
        REF_DATABASE_ROOT.child(NODE_SCHEDULE).child(UID).child(LocalDate.now().toString())
            .orderByChild(CHILD_TIME_START)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
                scheduleList = dataSnapshot.children.map {
                    it.getCommonModel()
                }
                scheduleList.forEach { model ->
                    REF_DATABASE_ROOT.child(NODE_SCHEDULE).child(UID)
                        .child(LocalDate.now().toString()).child(model.id)
                        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
                            val schedule = dataSnapshot1.getCommonModel()
                            scheduleAdapter.updateListSchedule(schedule)
                        }) } })
    }
}