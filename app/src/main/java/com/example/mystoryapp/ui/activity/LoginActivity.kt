package com.example.mystoryapp.ui.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.R
import com.example.mystoryapp.data.User
import com.example.mystoryapp.data.local.SessionManager
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.ui.viewmodel.LoginViewModel
import com.example.mystoryapp.ui.viewmodel.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var user: User? = null
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "masuk oncreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (intent.getParcelableExtra<User>(EXTRA_LOGIN) != null){
            user = intent.getParcelableExtra<User>(EXTRA_LOGIN) as User
        }

        binding.apply {
            progressBar.visibility = View.INVISIBLE
            edtEmail.setText(user?.email)
            edtPassword.setText(user?.password)
            btnToRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            btnLogin.setOnClickListener{
                setUpView()
                if(edtEmail.text.toString() == "" || edtEmail.text.toString() == "" ){
                    Toast.makeText(this@LoginActivity, getString(R.string.incomplete_form), Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.postLogin(edtEmail.text.toString(), edtPassword.text.toString())
                }
            }
        }
    }

    private fun setUpView() {

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        viewModel.user.observe(this){
            if(it != null){
                sessionManager.saveUserInfo(it.name, it.email, it.password)
                sessionManager.saveAuthToken(it.token)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }

//        viewModel.isSuccess.observe(this){
//            Log.e(TAG, it.toString())
//            if(it==true){
//                val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                Log.e(TAG, "masuk pengecekan sukses login")
//                startActivity(intent)
//                finish()
//            }
//        }

        viewModel.stringError.observe(this){
            if(it != null){
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        val TAG: String = LoginActivity::class.java.simpleName
        const val EXTRA_LOGIN = "extra_login"
    }
}