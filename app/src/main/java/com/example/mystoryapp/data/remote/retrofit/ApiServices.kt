package com.example.mystoryapp.data.remote.retrofit

import com.example.mystoryapp.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {

    @FormUrlEncoded
    @POST("register")
    @Headers("Authorization: token 12345")
    fun register(
        @Field("name") name:String,
        @Field("email") email:String,
        @Field("password") password:String
    ): Call<RegisterResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token : String,
        @Query("location") loc: Int = 0,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
    ): Call<StoryResponse>

    @GET("stories?location=1")
    fun getStoriesLocOn(@Header("Authorization") token : String ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") desc: RequestBody
    ): Call<PostStoryResponse>

    @FormUrlEncoded
    @Headers("Authorization: token 12345")
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>
}