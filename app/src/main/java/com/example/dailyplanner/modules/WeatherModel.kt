package com.example.dailyplanner.modules

import com.google.gson.annotations.SerializedName

class WeatherModel {
    @SerializedName("main")
    var main: Main? = null
    @SerializedName("weather")
    var weather = ArrayList<Weather>()
}

class Weather {
    @SerializedName("icon")
    var icon: String? = null
}

class Main {
    @SerializedName("temp")
    var temp: Float = 0.toFloat()
}