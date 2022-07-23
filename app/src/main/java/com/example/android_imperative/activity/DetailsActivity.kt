package com.example.android_imperative.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.android_imperative.R
import com.example.android_imperative.adapter.TVEpisodeAdapter
import com.example.android_imperative.adapter.TVShortAdapter
import com.example.android_imperative.databinding.ActivityDetailsBinding
import com.example.android_imperative.utils.Logger
import com.example.android_imperative.viewmodel.DetailsViewModel

class DetailsActivity : BaseActivity() {
    private val TAG = "TAG"+this::class.java.simpleName
    private lateinit var binding: ActivityDetailsBinding
    private val viewModel by viewModels<DetailsViewModel>()
    private val shortAdapter by lazy { TVShortAdapter() }
    private val episodeAdapter by lazy { TVEpisodeAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        initObservers()
        binding.rvShorts.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.ivClose.setOnClickListener {
            ActivityCompat.finishAfterTransition(this)
        }

        val iv_detail = binding.ivDetail
        val extras = intent.extras
        val show_id = extras!!.getLong("show_id")
        val show_img = extras.getString("show_img")
        val show_name = extras.getString("show_name")
        val show_network = extras.getString("show_network")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            val imageTransitionName = extras.getString("iv_movie")
            iv_detail.transitionName = imageTransitionName
        }
        binding.tvName.text = show_name
        binding.tvType.text = show_network
        Glide.with(this).load(show_img).into(iv_detail)

        viewModel.apiTVShowDetails(show_id!!.toInt())

        binding.rvShorts.adapter = shortAdapter
        binding.rvEpisode.adapter = episodeAdapter
    }

    private fun initObservers(){
        viewModel.tvShowDetails.observe(this){
            Logger.d(TAG, it.toString())
            binding.tvDetails.text = it.tvShow.description
            shortAdapter.submitList(it.tvShow.pictures)
            episodeAdapter.submitList(it.tvShow.episodes)
        }
        viewModel.errorMessage.observe(this){
            Logger.d(TAG, it.toString())
        }
        viewModel.isLoading.observe(this){
            Logger.d(TAG, it.toString())
            if (it){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        }
    }

}