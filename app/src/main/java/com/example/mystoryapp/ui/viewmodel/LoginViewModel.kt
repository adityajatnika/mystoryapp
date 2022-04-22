package com.example.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.mystoryapp.ResponseStatus
import com.example.mystoryapp.data.Story
import com.example.mystoryapp.data.User
import com.example.mystoryapp.data.local.SessionManager
import com.example.mystoryapp.data.remote.response.ListStoryItem
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
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        user.postValue(User(
                            responseBody.loginResult.name,
                            email,
                            pass,
                            responseBody.loginResult.token)
                        )
//                        }
                        isLoading.postValue(false)
                        Log.e(TAG, "masuk cek respon setelah courutine")
                        Log.e(TAG, responseBody.loginResult.token)
                    }
                } else {
                    val errorMessage :String = response.message().ifEmpty {
                        when (val statusCode = response.code()) {
                            ResponseStatus.BAD_REQUEST.stat -> "$statusCode : Bad Request"
                            ResponseStatus.FORBIDDEN.stat -> "$statusCode : Forbidden"
                            ResponseStatus.NOT_FOUND.stat -> "$statusCode : Not Found"
                            else -> "$statusCode"
                        }
                    }
                    stringError.postValue(errorMessage)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                isLoading.postValue(false)
                stringError.postValue(t.message)
                Log.e(TAG, t.message.toString())
                t.printStackTrace()
            }
        })
    }
    companion object {
        val TAG: String = LoginViewModel::class.java.simpleName
    }
}