package com.example.dailyplanner.database

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var AUTH: FirebaseAuth

const val NODE_USERS = "users"
const val NODE_SCHEDULE = "schedules"
const val NODE_SCHEDULE_USUAL = "schedules_usual"
const val NODE_NOTES = "notes"

const val CHILD_NAME = "name"
const val CHILD_EMAIL = "email"
const val CHILD_PASSWORD = "password"
const val CHILD_IMAGE_PROFILE_URL = "image_profile_url"

const val CHILD_ID = "id"
const val CHILD_NAME_LESSON = "name_lesson"
const val CHILD_TEACHER_NAME = "teacher_name"
const val CHILD_TYPE_LESSON = "type_lesson"
const val CHILD_CORPUS = "corpus"
const val CHILD_LECTURE_HALL = "lecture_hall"
const val CHILD_TIME_START = "time_start"
const val CHILD_TIME_END = "time_end"
const val CHILD_CONTENT = "content"

const val CHILD_NAME_SCHEDULE= "name_schedule"
const val CHILD_TASKS_DAY_SCHEDULE = "tasks_day_schedule"

const val CHILD_TITLE = "title"

const val CHILD_TEXT = "text"
const val CHILD_DATE = "date"


lateinit var REF_STORAGE_ROOT: StorageReference
const val FOLDER_PROFILE_IMAGE = "profile_image"

