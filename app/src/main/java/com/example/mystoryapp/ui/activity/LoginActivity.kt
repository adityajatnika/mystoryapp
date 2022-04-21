package com.example.mystoryapp.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.ResponseStatus
import com.example.mystoryapp.data.Story
import com.example.mystoryapp.data.local.AccountPreferences
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.ui.adapter.ListStoryAdapter
import com.example.mystoryapp.ui.viewmodel.LoginViewModel
import com.example.mystoryapp.ui.viewmodel.MainViewModel
import com.example.mystoryapp.ui.viewmodel.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account")

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToRegister.setOnClickListener(this)
        binding.progressBar.visibility = View.INVISIBLE
//        binding.btnLogin.setOnClickListener(this)

        val pref = AccountPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

        binding.apply {
            btnLogin.setOnClickListener{
                setUpView(loginViewModel)
                if(binding.edtEmail.text.toString() == "" || binding.edtEmail.text.toString() == "" ){
                    Toast.makeText(this@LoginActivity, "Harap lengkapi form terlebih dahulu", Toast.LENGTH_SHORT).show()
                } else {
                    loginViewModel.postLogin(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
                }
            }
        }
    }

    private fun setUpView(loginViewModel : LoginViewModel) {

        loginViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        loginViewModel.isSuccess.observe(this){
            if(it==true){
                val intent = Intent(this@LoginActivity, SplashScreen::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Autentikasi Gagal", Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.stringError.observe(this){
            if(it != null){
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_toRegister -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
//            R.id.btn_login -> {
//                setUpView()
//                if(binding.edtEmail.text.toString() == "" || binding.edtEmail.text.toString() == "" ){
//                    Toast.makeText(this, "Harap lengkapi form terlebih dahulu", Toast.LENGTH_SHORT).show()
//                } else {
//                    loginViewModel.postLogin(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
//                }
//            }
        }
    }
}