package com.example.mystoryapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.data.StoryRepository
import com.example.mystoryapp.data.di.Injection
import com.example.mystoryapp.data.remote.response.ListStoryItem

class MainViewModel(storyRepository: StoryRepository) : ViewModel(){

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

//    val story = MutableLiveData<List<Story>>()
//    val isLoading = MutableLiveData(true)
//    val stringError = MutableLiveData<String>()
//    val story2: LiveData<PagingData<ListStoryItem>> =
//        storyRepository.getQuote().cachedIn(viewModelScope)
//
//
//    private fun setListStory(responseBody: List<ListStoryItem>) {
//        val listStory = ArrayList<Story>()
//        for (item in responseBody) {
//            listStory.add(
//                Story(item.name, item.photoUrl, item.description)
//            )
//            Log.e(TAG, item.name)
//        }
//        story.postValue(listStory)
//    }
//
//    fun getListStory(token : String) {
//        isLoading.postValue(true)
//        val client = ApiConfig.getApiService().getStories(
//            token = StringBuilder("Bearer ").append(token).toString()
//        )
//        client.enqueue(object : Callback<StoryResponse> {
//            override fun onResponse(
//                call: Call<StoryResponse>,
//                response: Response<StoryResponse>
//            ) {
//                isLoading.postValue(false)
//                if (response.isSuccessful) {
//                    val responseBody = response.body()
//                    if (responseBody != null) {
//                        setListStory(responseBody.listStory)
//                    }
//                } else {
//                    val errorMessage :String = response.message().ifEmpty {
//                        when (val statusCode = response.code()) {
//                            ResponseStatus.BAD_REQUEST.stat -> "$statusCode : Bad Request"
//                            ResponseStatus.FORBIDDEN.stat -> "$statusCode : Forbidden"
//                            ResponseStatus.NOT_FOUND.stat -> "$statusCode : Not Found"
//                            else -> "$statusCode"
//                        }
//                    }
//                    stringError.postValue(errorMessage)
//                }
//            }
//
//            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
//                isLoading.postValue(false)
//                stringError.postValue(t.message)
//                Log.e(TAG, t.message.toString())
//                t.printStackTrace()
//            }
//        })
//    }
//    companion object {
//        val TAG: String = MainViewModel::class.java.simpleName
//    }

}

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}