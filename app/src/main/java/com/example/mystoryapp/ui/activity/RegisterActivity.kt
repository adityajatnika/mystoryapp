package com.example.mystoryapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mystoryapp.R
import com.example.mystoryapp.data.User
import com.example.mystoryapp.data.local.SessionManager
import com.example.mystoryapp.databinding.ActivityRegisterBinding
import com.example.mystoryapp.ui.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var sessionManager: SessionManager
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.btnToLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.progressBar.visibility = View.INVISIBLE


    }

    override fun onClick(v: View?) {
        when (v?.id){
            R.id.btn_toLogin -> {
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            R.id.btn_register -> {
                setUpView()
                binding.apply {
                    if(edtEmail.text.toString() == "" || edtEmail.text.toString() == "" ){
                        Toast.makeText(this@RegisterActivity, getString(R.string.incomplete_form), Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.postRegister(edtName.text.toString(), edtEmail.text.toString(), edtPassword.text.toString())
                    }
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

        viewModel.isSuccess.observe(this){
            if(it==true){
                val user = User(
                    binding.edtName.text.toString(),
                    binding.edtEmail.text.toString(),
                    binding.edtPassword.text.toString())
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                intent.putExtra(LoginActivity.EXTRA_LOGIN, user)
                Toast.makeText(this, "Selamat! Anda telah terdaftar", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
            }
        }

        viewModel.stringError.observe(this){
            if(it != null){
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }
}