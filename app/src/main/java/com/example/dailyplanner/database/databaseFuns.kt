package com.example.dailyplanner.database

import com.example.dailyplanner.modules.CommonModel
import com.example.dailyplanner.modules.UserModel
import com.example.dailyplanner.ui.calendar.CalendarUtils
import com.example.dailyplanner.utilities.APP_ACTIVITY
import com.example.dailyplanner.utilities.UID
import com.example.dailyplanner.utilities.restartActivity
import com.example.dailyplanner.utilities.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDate

fun initFireBase() {
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    AUTH = FirebaseAuth.getInstance()
    UID = AUTH.currentUser?.uid.toString()

    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}


fun createAndPushScheduleToDatabase(
    name_lesson: String,
    teacher_name: String,
    type_lesson: String,
    corpus: String,
    lecture_hall: String,
    timeStart: String,
    timeEnd: String,
    selectedDate: String,
    function: () -> Unit
) {
    val key =
        REF_DATABASE_ROOT.child(NODE_SCHEDULE).child(UID).child(selectedDate).push().key.toString()
    val path = REF_DATABASE_ROOT.child(NODE_SCHEDULE).child(UID).child(selectedDate).child(key)
    val mapData = hashMapOf<String, Any>()
    mapData[CHILD_ID] = key
    mapData[CHILD_NAME_LESSON] = name_lesson
    mapData[CHILD_TEACHER_NAME] = teacher_name
    mapData[CHILD_TYPE_LESSON] = type_lesson
    mapData[CHILD_LECTURE_HALL] = lecture_hall
    mapData[CHILD_CORPUS] = corpus
    mapData[CHILD_TIME_START] = timeStart
    mapData[CHILD_TIME_END] = timeEnd
    path.updateChildren(mapData)
        .addOnSuccessListener {
            showToast("Успех")
            function()
        }.addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun updateScheduleDatabase(
    name_lesson: String,
    teacher_name: String,
    type_lesson: String,
    corpus: String,
    lecture_hall: String,
    timeStart: String,
    timeEnd: String,
    key: String,
    selectedDate: String,
    function: () -> Unit
) {
    val path = REF_DATABASE_ROOT.child(NODE_SCHEDULE).child(UID).child(selectedDate).child(key)
    val mapData = hashMapOf<String, Any>()
    mapData[CHILD_NAME_LESSON] = name_lesson
    mapData[CHILD_TEACHER_NAME] = teacher_name
    mapData[CHILD_TYPE_LESSON] = type_lesson
    mapData[CHILD_LECTURE_HALL] = lecture_hall
    mapData[CHILD_CORPUS] = corpus
    mapData[CHILD_TIME_START] = timeStart
    mapData[CHILD_TIME_END] = timeEnd
    path.updateChildren(mapData)
        .addOnSuccessListener {
            showToast("Успех")
            function()
        }.addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun createAndPushScheduleUsualToDatabase(
    name_schedule: String,
    tasks_day_schedule: String,
    timeStart: String,
    timeEnd: String,
    selectedDate: String,
    function: () -> Unit
) {
    val key = REF_DATABASE_ROOT.child(NODE_SCHEDULE_USUAL).child(UID).child(selectedDate)
        .push().key.toString()
    val path =
        REF_DATABASE_ROOT.child(NODE_SCHEDULE_USUAL).child(UID).child(selectedDate).child(key)
    val mapData = hashMapOf<String, Any>()
    mapData[CHILD_ID] = key
    mapData[CHILD_NAME_SCHEDULE] = name_schedule
    mapData[CHILD_TASKS_DAY_SCHEDULE] = tasks_day_schedule
    mapData[CHILD_TIME_START] = timeStart
    mapData[CHILD_TIME_END] = timeEnd
    path.updateChildren(mapData)
        .addOnSuccessListener {
            showToast("Успех")
            function()
        }.addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun updateScheduleUsualDatabase(
    name_schedule: String,
    tasks_day_schedule: String,
    timeStart: String,
    timeEnd: String,
    key: String,
    selectedDate: String,
    function: () -> Unit
) {
    val path =
        REF_DATABASE_ROOT.child(NODE_SCHEDULE_USUAL).child(UID).child(selectedDate).child(key)
    val mapData = hashMapOf<String, Any>()
    mapData[CHILD_NAME_SCHEDULE] = name_schedule
    mapData[CHILD_TASKS_DAY_SCHEDULE] = tasks_day_schedule
    mapData[CHILD_TIME_START] = timeStart
    mapData[CHILD_TIME_END] = timeEnd
    path.updateChildren(mapData)
        .addOnSuccessListener {
            showToast("Успех")
            function()
        }.addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun createAndPushNoteToDatabase(
    title: String,
    text: String,
    date: String,
    function: () -> Unit
) {
    val key = REF_DATABASE_ROOT.child(NODE_NOTES).child(UID).push().key.toString()
    val path = REF_DATABASE_ROOT.child(NODE_NOTES).child(UID).child(key)
    val mapData = hashMapOf<String, Any>()
    mapData[CHILD_ID] = key
    mapData[CHILD_TITLE] = title
    mapData[CHILD_TEXT] = text
    mapData[CHILD_DATE] = date
    path.updateChildren(mapData)
        .addOnSuccessListener {
            showToast("Успех")
            function()
        }.addOnFailureListener {
            showToast(it.message.toString())
        }
}

fun updateNoteDatabase(
    title: String,
    text: String,
    date: String,
    key: String,
    function: () -> Unit
) {
    val path = REF_DATABASE_ROOT.child(NODE_NOTES).child(UID).child(key)
    val mapData = hashMapOf<String, Any>()
    mapData[CHILD_TITLE] = title
    mapData[CHILD_TEXT] = text
    mapData[CHILD_DATE] = date
    path.updateChildren(mapData)
        .addOnSuccessListener {
            showToast("Успех")
            function()
        }.addOnFailureListener {
            showToast(it.message.toString())
        }
}


fun DataSnapshot.getCommonModel(): CommonModel =
    this.getValue(CommonModel::class.java) ?: CommonModel()

fun DataSnapshot.getUserModel(): UserModel =
    this.getValue(UserModel::class.java) ?: UserModel()

fun createUserUploadToDatabase(
    name: String,
    email: String,
    password: String,
    function: () -> Unit
) {
    AUTH.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
        val pushData = hashMapOf<String, Any>()
        UID = AUTH.currentUser?.uid.toString()
        pushData[CHILD_ID] = UID
        pushData[CHILD_NAME] = name
        pushData[CHILD_EMAIL] = email
        pushData[CHILD_PASSWORD] = password
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).updateChildren(pushData)
            .addOnSuccessListener {
                function()
            }.addOnFailureListener {
                showToast(it.message.toString())
            }
    }.addOnFailureListener {
        showToast(it.message.toString())
    }
}

fun firebaseAuthWithGoogle(
    idToken: String,
    name: String,
    email: String,
    photoUrl: String,
    function: () -> Unit
) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    AUTH.signInWithCredential(credential).addOnCompleteListener {
        val pushData = hashMapOf<String, Any>()
        UID = AUTH.currentUser?.uid.toString()
        pushData[CHILD_ID] = UID
        pushData[CHILD_NAME] = name
        pushData[CHILD_EMAIL] = email
        pushData[CHILD_IMAGE_PROFILE_URL] = photoUrl
        if (it.isSuccessful) {
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).updateChildren(pushData)
                .addOnSuccessListener {
                    function()
                }.addOnFailureListener {
                    showToast(it.message.toString())
                }
        }
    }
}
