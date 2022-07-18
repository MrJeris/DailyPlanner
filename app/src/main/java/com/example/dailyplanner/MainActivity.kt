package com.example.dailyplanner

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.dailyplanner.database.*
import com.example.dailyplanner.databinding.ActivityMainBinding
import com.example.dailyplanner.modules.WeatherModel
import com.example.dailyplanner.ui.calendar.CalendarFragment
import com.example.dailyplanner.ui.notes.NotesFragment
import com.example.dailyplanner.ui.register.SignInFragment
import com.example.dailyplanner.ui.schedule.ScheduleFragment
import com.example.dailyplanner.ui.schedule.ScheduleUsualFragment
import com.example.dailyplanner.ui.settings.SettingsFragment
import com.example.dailyplanner.utilities.*
import com.example.dailyplanner.weatherAPI.ServiceBuilder
import com.example.dailyplanner.weatherAPI.WeatherService
import com.squareup.picasso.Picasso
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        APP_ACTIVITY = this
        APP_ACTIVITY.binding.bottomNavigationMenuRoot.visibility = View.VISIBLE
        APP_ACTIVITY.binding.mainToolbar.visibility = View.VISIBLE
        initFireBase()
        initFunc()
        binding.bottomNavigationMenu.background = null
        binding.bottomNavigationMenu.menu.getItem(2).isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        initLocation()
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeUpdates(locationListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Override
    private fun initFunc() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(AppValueEventListener {
                USER = it.getUserModel()
            })
        binding.bottomNavigationMenu.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> changingScreen()
                R.id.navigation_calendar -> replaceFragment(CalendarFragment())
                R.id.navigation_notes -> replaceFragment(NotesFragment())
                R.id.navigation_settings -> replaceFragment(SettingsFragment())
            }
            true
        }
        checkingUserAuthorization()
        dateOutputToolbar()
    }

    private fun checkingUserAuthorization() {
        if (AUTH.currentUser != null) {
            binding.bottomNavigationMenuRoot.visibility = View.VISIBLE
            binding.mainToolbar.visibility = View.VISIBLE
            replaceFragment(ScheduleFragment(), false)
        } else {
            binding.bottomNavigationMenuRoot.visibility = View.GONE
            binding.mainToolbar.visibility = View.GONE
            replaceFragment(SignInFragment(), false)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun dateOutputToolbar() {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMMM, eeee")
        binding.mainToolbarDate.text = LocalDate.now().format(formatter).toString()
    }

    private fun changingScreen() {
        val menuItem = binding.bottomNavigationMenu.menu.findItem(R.id.navigation_home)

        if (menuItem.isChecked) {
            if (menuItem.title == "Учебный") {
                menuItem.title =
                    "Обычный"
                replaceFragment(ScheduleUsualFragment())
            } else if (menuItem.title == "Обычный") {
                menuItem.title =
                    "Учебный"
                replaceFragment(ScheduleFragment())
            }
        }
        if (menuItem.title == "Учебный") {
            replaceFragment(ScheduleFragment())
        } else if (menuItem.title == "Обычный") {
            replaceFragment(ScheduleUsualFragment())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun weatherAPI(lat: String, lon: String) {
        val weatherService = ServiceBuilder.buildService(WeatherService::class.java)
        val requestCall = weatherService.getWeather(lat, lon)
        requestCall.enqueue(AppCallBack<WeatherModel> {
            if (it.isSuccessful) {
                val receivedWeather = it.body()!!
                Picasso.get()
                    .load("https://openweathermap.org/img/wn/${receivedWeather.weather[0]
                        .icon.toString()}@2x.png")
                    .into(binding.toolbarWeatherImage)
                binding.toolbarTemperature.text = "${receivedWeather.main!!.temp.toInt()}\u2103"
            } else showToast("Что-то пошло не так с получением погоды") })
    }

    private fun initLocation() {
        locationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager

        if (checkPermission(FINE_LOCATION) && checkPermission(COARSE_LOCATION)) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10 * 1000,
                1000f,
                locationListener
            )
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                10 * 1000,
                1000f,
                locationListener) } }

    private val locationListener: LocationListener =
        LocationListener { location -> showLocation(location) }

    private fun showLocation(location: Location?) {
        if (location!!.provider.equals(LocationManager.GPS_PROVIDER)) {
            val lat: String = location.latitude.toString()
            val lon: String = location.longitude.toString()
            weatherAPI(lat, lon)
        } else if (location!!.provider.equals(LocationManager.NETWORK_PROVIDER)) {
            val lat: String = location.latitude.toString()
            val lon: String = location.longitude.toString()
            weatherAPI(lat, lon) } }
}