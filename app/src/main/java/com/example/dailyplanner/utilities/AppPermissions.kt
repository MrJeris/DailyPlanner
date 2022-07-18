package com.example.dailyplanner.utilities

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

const val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
const val COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
const val PERMISSION_REQUEST = 200

fun checkPermission(permission: String): Boolean {
    return if (ContextCompat.checkSelfPermission(
            APP_ACTIVITY,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(APP_ACTIVITY, arrayOf(permission), PERMISSION_REQUEST)
        false
    } else true
}