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