package com.example.mystoryapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.mystoryapp.ResponseStatus
import com.example.mystoryapp.data.Story
import com.example.mystoryapp.data.local.SessionManager
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.remote.response.StoryResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel(){
    val story = MutableLiveData<List<Story>>()
    val isLoading = MutableLiveData(true)
    val stringError = MutableLiveData<String>()
    private lateinit var sessionManager: SessionManager

//    fun getToken(): LiveData<String> {
//        return pref.getToken().asLiveData()
//    }

//    fun logout(){
////        viewModelScope.launch {
////            pref.setToken("")
////            pref.saveAccountLogin("", "", "")
////        }
//        sessionManager.saveUserInfo("","","")
//        sessionManager.saveAuthToken(null)
//    }

    private fun setListStory(responseBody: List<ListStoryItem>) {
        val listStory = ArrayList<Story>()
        for (item in responseBody) {
            listStory.add(
                Story(item.name, item.photoUrl, item.description)
            )
        }
        story.postValue(listStory)
    }

    fun getListStory(token : String) {
        isLoading.postValue(true)
        val client = ApiConfig.getApiService().getStories(
            token = StringBuilder("Bearer ").append(token).toString()
        )
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                isLoading.postValue(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setListStory(responseBody.listStory)
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
                    Log.e(LoginViewModel.TAG, errorMessage)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                isLoading.postValue(false)
                stringError.postValue("autentikasi gagal")
                Log.e(TAG, t.message.toString())
                t.printStackTrace()
            }
        })
    }
    companion object {
        val TAG: String = MainViewModel::class.java.simpleName
    }

}