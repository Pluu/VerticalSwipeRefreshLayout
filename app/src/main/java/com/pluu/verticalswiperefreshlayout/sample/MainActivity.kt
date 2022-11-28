package com.pluu.verticalswiperefreshlayout.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.verticalswiperefreshlayout.sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFinish.setOnClickListener {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
}