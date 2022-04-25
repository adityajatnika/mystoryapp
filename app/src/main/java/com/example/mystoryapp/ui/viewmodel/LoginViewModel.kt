package com.example.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.mystoryapp.R
import com.example.mystoryapp.ResponseStatus
import com.example.mystoryapp.data.Story
import com.example.mystoryapp.data.User
import com.example.mystoryapp.data.local.SessionManager
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import com.example.mystoryapp.ui.activity.LoginActivity
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
                Log.e(TAG, "masuk response")
                Log.e(TAG, response.message())
                val responseBody = response.body()
                isLoading.postValue(false)
                if (response.isSuccessful && responseBody != null){
                    Log.e(TAG, "respons sukses")
                    user.postValue(User(
                        responseBody.loginResult.name,
                        email,
                        pass,
                        responseBody.loginResult.token)
                    )
                    Log.e(TAG, responseBody.loginResult.token)
                } else if (responseBody != null){
                    Log.e(TAG, "respons gagal")
                    val errorMsg: String = responseBody.message
                    stringError.postValue(errorMsg)
                } else {
                    Log.e(TAG, "respons kosong euy")
                    stringError.postValue(response.message().toString())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(LoginActivity.TAG, "gagal response")
                isLoading.postValue(false)
                stringError.postValue(R.string.autentication_failed.toString())
                Log.e(TAG, t.message.toString())
                t.printStackTrace()
            }
        })
    }
    companion object {
        val TAG: String = LoginViewModel::class.java.simpleName
    }
}