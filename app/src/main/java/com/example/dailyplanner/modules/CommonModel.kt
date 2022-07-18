package com.example.dailyplanner.modules

class CommonModel(
    val id: String = "",
    var name_lesson: String = "",
    var teacher_name: String = "",
    var type_lesson: String = "",
    var lecture_hall: String = "",
    var corpus: String = "",
    var time_end: String = "",
    var time_start: String = "",
    var content: String = "",

    var name_schedule: String = "",
    var tasks_day_schedule: String = "",

    var title: String = "",
    var text: String = "",
    var date: String = ""
) {
    override fun equals(other: Any?): Boolean {
        return (other as CommonModel).id == id
    }
}