package com.example.mystoryapp.data.remote.retrofit

import com.example.mystoryapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password:String
    ): Call<RegisterResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token : String,
        @Query("location") loc: Int = 0,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoryResponse<ListStoryItem>
    
    @GET("stories")
    fun getStoriesLocOn(
        @Header("Authorization") token : String,
        @Query("location") loc: Int = 1,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Call<StoryResponse<ListStoryItem>>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") desc: RequestBody,
    ): Call<PostStoryResponse>

    @Multipart
    @POST("stories")
    fun postStoryLocOn(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") desc: RequestBody,
        @Part("lat") lat: Float,
        @Part("lon") lon: Float
    ): Call<PostStoryResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>
}