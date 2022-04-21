package com.example.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.mystoryapp.ResponseStatus
import com.example.mystoryapp.data.local.AccountPreferences
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: AccountPreferences) : ViewModel() {
    val isLoading = MutableLiveData(false)
    val stringError = MutableLiveData<String>()
    val isSuccess = MutableLiveData(false)

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun setAccount(name : String, email : String, pass : String){
        viewModelScope.launch {
            pref.saveAccountLogin(name, email, pass)
        }
    }

    fun setToken(token : String){
        viewModelScope.launch {
            pref.setToken(token)
        }
    }

    fun postLogin(email : String, pass : String){
        isLoading.postValue(true)
        val client = ApiConfig.getApiService().login(email, pass)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                isLoading.postValue(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setAccount(responseBody.loginResult.name, email, pass)
                        setToken(responseBody.loginResult.token)
                        isSuccess.postValue(true)
                    }
                } else {
                    val errorMessage = when (val statusCode = response.code()) {
                        ResponseStatus.BAD_REQUEST.stat -> "$statusCode : Bad Request"
                        ResponseStatus.FORBIDDEN.stat -> "$statusCode : Forbidden"
                        ResponseStatus.NOT_FOUND.stat -> "$statusCode : Not Found"
                        else -> "$statusCode"
                    }
                    Log.e(TAG, errorMessage)
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