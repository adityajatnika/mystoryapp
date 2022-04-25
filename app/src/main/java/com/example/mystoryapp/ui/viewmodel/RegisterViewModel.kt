package com.example.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mystoryapp.ResponseStatus
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.response.RegisterResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel: ViewModel() {

    val isSuccess = MutableLiveData(false)
    val isLoading = MutableLiveData(false)
    val stringError = MutableLiveData<String>()

    fun postRegister(name: String, email: String, pass: String) {
        isLoading.postValue(true)
        val client = ApiConfig.getApiService().register(name, email, pass)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                Log.e(LoginViewModel.TAG, "masuk response")
                isLoading.postValue(false)
                if (response.isSuccessful) {
                    Log.e(LoginViewModel.TAG, "masuk sukses")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        isSuccess.postValue(true)
                    }
                } else {
                    Log.e(LoginViewModel.TAG, "masuk gagal")
                    val errorMessage :String = response.message().ifEmpty {
                        when (val statusCode = response.code()) {
                            ResponseStatus.BAD_REQUEST.stat -> "$statusCode : Bad Request"
                            ResponseStatus.FORBIDDEN.stat -> "$statusCode : Forbidden"
                            ResponseStatus.NOT_FOUND.stat -> "$statusCode : Not Found"
                            else -> "$statusCode"
                        }
                    }
                    stringError.postValue(errorMessage)
                    Log.e(LoginViewModel.TAG, errorMessage)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(LoginViewModel.TAG, "masuk failure")
                isLoading.postValue(false)
                stringError.postValue(t.message)
                Log.e(LoginViewModel.TAG, t.message.toString())
                t.printStackTrace()
            }
        })
    }


}