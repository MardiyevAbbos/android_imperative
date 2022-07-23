package com.example.android_imperative.activity

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android_imperative.R
import com.example.android_imperative.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private val TAG = "TAG" + this::class.simpleName.toString()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {
        val bnv = binding.bnvMain
        navController = findNavController(R.id.nav_host_fragment_container)
        bnv.setupWithNavController(navController)
    }

}