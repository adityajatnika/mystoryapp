package com.example.mystoryapp.data.remote.retrofit

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.SharedPreferences
import com.example.mystoryapp.data.local.SessionManager
import com.example.mystoryapp.ui.activity.MainActivity
import com.example.mystoryapp.ui.activity.MySuperAppApplication.Companion.context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
//    private lateinit var sessionManager : SessionManager
//    private var context: Context? = null
//    fun ApiConfig(context: Context?) {
//        this.context = context
//    }
    companion object{

        fun getApiService(): ApiServices {
            //Save token here
//            val token = "Some token From Server";
////            val intent = Intent(this@, MainActivity::class.java)
//            val preferences: SharedPreferences= getActivity().getSharedPreferences("MY_APP",
//                Context.MODE_PRIVATE);
//            preferences.edit().putString("TOKEN",token).apply()

            val pref = AuthInterceptor(sessionService = SessionManager(MainActivity.context))
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
//                .addInterceptor(pref)
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiServices::class.java)
        }
    }
}

class AuthInterceptor(private val sessionService: SessionManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val req = chain.request()
        val basicReqBuilder = req.newBuilder()
        basicReqBuilder.addHeader("Authorization", "Bearer " + sessionService.fetchAuthToken())
            .build()
        return chain.proceed(req)
    }
}