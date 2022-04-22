package com.example.mystoryapp.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.data.Story
import com.example.mystoryapp.data.local.SessionManager
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.ui.adapter.ListStoryAdapter
import com.example.mystoryapp.ui.viewmodel.MainViewModel
import java.lang.StringBuilder

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "account")

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var sessionManager: SessionManager
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
//    private lateinit var token: LiveData<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "masuk oncreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = getString(R.string.list_story)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
//        token = viewModel.getToken()
        binding.btnAdd.setOnClickListener(this)
        binding.rvStory.setHasFixedSize(true)

        setUpView(viewModel)
        Log.e(TAG, "masuk mau load page")
        loadPage(viewModel)
    }

    private fun loadPage(viewModel: MainViewModel) {
        Log.e(TAG, "masuk load page")
        val token = sessionManager.fetchAuthToken()
        Log.e(TAG, token.toString())
        if(token != null){
            Log.e(TAG, "masuk load page main")
            viewModel.getListStory(token)
        }  else {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            Log.e(TAG, "masuk load page main else ke login")
            startActivity(intent)
            finish()
        }


//        viewModel.getToken().observe(this
//        ){
//            Log.e(TAG, it.toString())
//            if (it != "") {
//                viewModel.getListStory(it.toString())
//                Log.e(TAG, "masuk load page ke main")
//            } else {
//                val intent = Intent(this@MainActivity, LoginActivity::class.java)
//                Log.e(TAG, "masuk load page main else ke login")
//                startActivity(intent)
//                finish()
//            }
//        }
    }

    private fun setUpView(viewModel: MainViewModel) {
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
                loadPage(viewModel)
                true
            }
            R.id.menu_settings -> {
                Toast.makeText(this, "Menu Belum Tersedia", Toast.LENGTH_LONG).show()
                //                val i = Intent(this, MenuActivity::class.java)
                //                startActivity(i)
                true
            }
            R.id.menu_logout -> {
                Log.e(TAG, "masuk logout")
                sessionManager.saveUserInfo("", "", "")
                sessionManager.saveAuthToken(null)
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                Log.e(TAG, "masuk mau ke finish")
                finish()
                Log.e(TAG, "masuk setelah finish")
                true
            }
            else -> true
        }
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
        const val EXTRA_LOGIN = "extra_login"
    }
}