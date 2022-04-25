package com.example.mystoryapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.data.Story
import com.example.mystoryapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_story)

        supportActionBar?.title = getString(R.string.detail_story)

        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            val story = intent.getParcelableExtra<Story>(EXTRA_STORY) as Story
            Glide.with(applicationContext)
                .load(story.photo)
                .into(imgPhoto)
            tvName.text = story.name
            tvDesc.text = story.desc
        }
    }


    companion object{
        const val EXTRA_STORY = "extra_story"
    }
}