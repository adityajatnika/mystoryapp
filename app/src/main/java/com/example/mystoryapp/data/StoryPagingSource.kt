package com.example.mystoryapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mystoryapp.ResponseStatus
import com.example.mystoryapp.data.remote.retrofit.ApiServices
import com.example.mystoryapp.data.local.SessionManager
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.remote.response.StoryResponse
import com.example.mystoryapp.ui.viewmodel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class StoryPagingSource(private val apiService: ApiServices) : PagingSource<Int, ListStoryItem>() {
    private lateinit var sessionManager: SessionManager

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            Log.e(this.toString(), "sampai pagingsource")
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = sessionManager.fetchAuthToken()
            val responseData = apiService.getStories(StringBuilder("Bearer ").append(token).toString(), 1, position, 10)
            val data = responseData.execute().body()!!.listStory

            LoadResult.Page(
                data = data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (data.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}