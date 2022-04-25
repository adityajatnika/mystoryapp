package com.example.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.mystoryapp.R
import com.example.mystoryapp.data.User
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    val user = MutableLiveData<User>()
    val isLoading = MutableLiveData(false)
    val stringError = MutableLiveData<String>()

    fun postLogin(email : String, pass : String){
        isLoading.postValue(true)
        val client = ApiConfig.getApiService().login(email, pass)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                Log.e(TAG, response.message())
                val responseBody = response.body()
                isLoading.postValue(false)
                if (response.isSuccessful && responseBody != null){
                    user.postValue(User(
                        responseBody.loginResult.name,
                        email,
                        pass,
                        responseBody.loginResult.token)
                    )
                    Log.e(TAG, responseBody.loginResult.token)
                } else if (responseBody != null){
                    val errorMsg: String = responseBody.message
                    stringError.postValue(errorMsg)
                } else {
                    stringError.postValue(response.message().toString())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isLoading.postValue(false)
                stringError.postValue(R.string.authentication_failed.toString())
                Log.e(TAG, t.message.toString())
                t.printStackTrace()
            }
        })
    }
    companion object {
        val TAG: String = LoginViewModel::class.java.simpleName
    }
}