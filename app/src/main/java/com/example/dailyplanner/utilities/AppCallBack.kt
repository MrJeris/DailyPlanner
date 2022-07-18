package com.example.dailyplanner.utilities

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppCallBack<T>(val onReturn: (Response<T>) -> Unit): Callback<T> {
    override fun onResponse(call: Call<T>, response: Response<T>) {
        onReturn(response)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        showToast("Ошибка с API погода")
    }
}