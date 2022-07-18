package com.example.dailyplanner.ui.calendar

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

class CalendarUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    var selectedDate: LocalDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    public fun monthYearFromDate(date: LocalDate): String {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM y, eeee")
        return date.format(formatter)
    }

    //Функция заполнения массива числами для их отображения по дням недели (месячное представление)
    @RequiresApi(Build.VERSION_CODES.O)
    public fun daysInMonthArray(date: LocalDate): ArrayList<LocalDate> {
        val daysInMonthArray: ArrayList<LocalDate> = ArrayList()
        val yearMonth: YearMonth = YearMonth.from(date)
        val daysInMonth: Int = yearMonth.lengthOfMonth()
        val firstOfMonth = date.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value - 1

        val firstOfNextMonth = date.plusMonths(1).withDayOfMonth(1)
        val dayOfNextWeek = firstOfNextMonth.dayOfMonth - 1

        val daysInPrevMonth: Int = yearMonth.minusMonths(1).lengthOfMonth()
        val firstOfPrevMonth = date.minusMonths(1)
            .withDayOfMonth(daysInPrevMonth - dayOfWeek - 1)
        val dayOfPrevWeek = firstOfPrevMonth.dayOfMonth

        for (i in 1..42) {
            when {
                i <= dayOfWeek -> {
                    daysInMonthArray.add(LocalDate.of(selectedDate.year,
                        selectedDate.month - 1, i + dayOfPrevWeek + 1))
                }
                i > daysInMonth + dayOfWeek -> {
                    daysInMonthArray.add(
                        LocalDate.of(selectedDate.year, selectedDate.month + 1,
                            i - daysInMonth - dayOfWeek + dayOfNextWeek))
                }
                else -> daysInMonthArray.add(
                    LocalDate.of(selectedDate.year, selectedDate.month, i - dayOfWeek)
                )
            }
        }
        return daysInMonthArray
    }

    //Функция заполнения массива числами для их отображения по дням недели (недельное представление)
    @RequiresApi(Build.VERSION_CODES.O)
    public fun daysInWeekArray(selectedDate: LocalDate): ArrayList<LocalDate> {
        val days: ArrayList<LocalDate> = ArrayList()
        var current: LocalDate = mondayForDate(selectedDate)
        val endDate: LocalDate = current.plusWeeks(1)

        while (current.isBefore(endDate)) {
            days.add(current)
            current = current.plusDays(1)
        }
        return days
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mondayForDate(current: LocalDate): LocalDate {
        var current1 = current
        val oneWeekAgo: LocalDate = current1.minusWeeks(1)
        while (current1.isAfter(oneWeekAgo)) {
            if (current1.dayOfWeek == DayOfWeek.MONDAY)
                return current1
            current1 = current1.minusDays(1)
        }
        return LocalDate.MIN
    }
}