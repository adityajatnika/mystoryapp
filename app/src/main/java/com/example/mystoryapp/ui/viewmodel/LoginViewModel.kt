package com.example.mystoryapp.ui.viewmodel

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
    val stringError = MutableLiveData<String?>()

    fun postLogin(email : String, pass : String){
        isLoading.postValue(true)
        val client = ApiConfig.getApiService().login(email, pass)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                val responseBody = response.body()
                isLoading.postValue(false)
                if (response.isSuccessful && responseBody != null){
                    user.postValue(User(
                        responseBody.loginResult.name,
                        email,
                        pass,
                        responseBody.loginResult.token)
                    )
                } else if (responseBody != null){
                    stringError.postValue(responseBody.message)
                } else {
                    stringError.postValue(response.message().toString())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isLoading.postValue(false)
                stringError.postValue(R.string.authentication_failed.toString())
                t.printStackTrace()
            }
        })
    }
}