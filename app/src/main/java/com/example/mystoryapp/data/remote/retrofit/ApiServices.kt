package com.example.githubuserapp.data.remote.retrofit

//import com.example.mystoryapp.data.remote.response.DetailUserResponse
//import com.example.mystoryapp.data.remote.response.FindUserResponse
//import com.example.mystoryapp.data.remote.response.UserResponse
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.response.PostStoryResponse
import com.example.mystoryapp.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


//interface ApiServices {
//    @GET("users")
//    @Headers("Authorization: token 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")
//    fun getUsers(): Call<List<UserResponse>>
//
//    @GET("search/users")
//    @Headers("Authorization: token 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")
//    fun findUsers(@Query("q") query: String): Call<FindUserResponse>
//
//    @GET("users/{login}/followers")
//    @Headers("Authorization: token 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")
//    fun getFollowers(@Path("login") login : String): Call<List<UserResponse>>
//
//    @GET("users/{login}/following")
//    @Headers("Authorization: token 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")
//    fun getFollowing(@Path("login") login : String): Call<List<UserResponse>>
//
//    @GET("users/{login}")
//    @Headers("Authorization: token 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")
//    fun getDetailUser(@Path("login") login : String): Call<DetailUserResponse>
//}

interface ApiServices {
    @GET("register")
    @Headers()
    fun register()

    @GET("stories")
    fun getStories(@Header("Authorization") token : String ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
//        @Field("photo") photo: String,
        @Part("description") desc: RequestBody
    ): Call<PostStoryResponse>

//    @FormUrlEncoded
//    @Headers("Authorization: token 12345")
//    @POST("review")
//    fun postStory(
//        @Field("photo") photo: MultipartBody.Part,
//        @Field("desc") desc: RequestBody
//    ): Call<PostStoryResponse>

//    @GET("driver/v1/driver")
//    fun getAuthorizedDriver(@Header("authorization") auth: String?): Call<Driver?>?

    @FormUrlEncoded
    @Headers("Authorization: token 12345")
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>



//    @GET("search/users")
//    @Headers("Authorization: token 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")
//    fun findUsers(@Query("q") query: String): Call<FindUserResponse>
//
//    @GET("users/{login}/followers")
//    @Headers("Authorization: token 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")
//    fun getFollowers(@Path("login") login : String): Call<List<UserResponse>>
//
//    @GET("users/{login}/following")
//    @Headers("Authorization: token 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")
//    fun getFollowing(@Path("login") login : String): Call<List<UserResponse>>
//
//    @GET("users/{login}")
//    @Headers("Authorization: token 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")
//    fun getDetailUser(@Path("login") login : String): Call<DetailUserResponse>
}