package com.example.mystoryapp.ui.activity

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.data.local.pref.SessionManager
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.ui.adapter.ListStoryAdapter
import com.example.mystoryapp.ui.adapter.LoadingStateAdapter
import com.example.mystoryapp.ui.viewmodel.MainViewModel
import com.example.mystoryapp.ui.viewmodel.ViewModelFactory


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var sessionManager: SessionManager
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.list_story)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance = this

        sessionManager = SessionManager(this)
        binding.btnAdd.setOnClickListener(this)
        binding.rvStory.setHasFixedSize(true)

        setUpView()
        getData()
    }


    private fun getData() {

        val token = sessionManager.fetchAuthToken()
        binding.progressBar.visibility = View.INVISIBLE
        if(token != null){
            Log.e(TAG, "sampai getdata main")
            val adapter = ListStoryAdapter()
            binding.rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            viewModel.story.observe(this) {
                adapter.submitData(lifecycle, it)
            }

        } else {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setUpView() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStory.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStory.layoutManager = LinearLayoutManager(this)
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
            R.id.menu_map -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                true
            }
            R.id.menu_refresh -> {
                getData()
                true
            }
            R.id.menu_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.menu_logout -> {
                sessionManager.saveUserInfo("", "", "")
                sessionManager.saveAuthToken(null)
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> true
        }
    }
    companion object{
        private var instance: AppCompatActivity? = null
        val context: Context
            get() = instance!!.baseContext
    }
}