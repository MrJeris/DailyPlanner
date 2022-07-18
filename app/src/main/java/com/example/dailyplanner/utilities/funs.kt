package com.example.dailyplanner.utilities

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dailyplanner.MainActivity
import com.example.dailyplanner.R

fun replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(
                R.id.data_container, fragment
            ).commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(
                R.id.data_container, fragment
            ).commit()
    }
}

fun showToast(massage: String) {
    Toast.makeText(APP_ACTIVITY, massage, Toast.LENGTH_SHORT).show()
}

fun restartActivity() {
    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()
}