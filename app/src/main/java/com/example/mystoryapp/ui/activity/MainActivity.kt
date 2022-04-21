package com.example.mystoryapp.ui.activity

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.data.Story
import com.example.mystoryapp.data.local.AccountPreferences
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.ui.adapter.ListStoryAdapter
import com.example.mystoryapp.ui.viewmodel.LoginViewModel
import com.example.mystoryapp.ui.viewmodel.MainViewModel
import com.example.mystoryapp.ui.viewmodel.ViewModelFactory
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var token: LiveData<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.list_story)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AccountPreferences.getInstance(dataStore)
        val loginViewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]

        token = loginViewModel.getToken()

        binding.btnAdd.setOnClickListener(this)
        binding.rvStory.setHasFixedSize(true)

        setUpView()
        loadPage(token)
    }

    private fun loadPage(token: LiveData<String>) {
        token.observe(this
        ){
            if (it != "") {
                viewModel.getListStory(it.toString())
            } else {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setUpView() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
        }

        viewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }

        viewModel.story.observe(this) {
            if(it != null){
                val adapter = ListStoryAdapter(it)
                binding.rvStory.adapter = adapter

                adapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: Story) {
                        Toast.makeText(this@MainActivity, StringBuilder("desc : ").append(data.desc), Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        viewModel.stringError.observe(this){
            if(it != null){
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
    }

    //action bar
    override fun onClick(v: View?) {
        val intent = Intent(this@MainActivity, StoryActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_refresh -> {
                loadPage(token)
                true
            }
            R.id.menu_settings -> {
                Toast.makeText(this, "Menu Belum Tersedia", Toast.LENGTH_LONG).show()
    //                val i = Intent(this, MenuActivity::class.java)
    //                startActivity(i)
                true
            }
            else -> true
        }
    }
}