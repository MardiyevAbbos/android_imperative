package com.example.android_imperative.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_imperative.activity.DetailsActivity
import com.example.android_imperative.adapter.TVShowAdapter
import com.example.android_imperative.databinding.FragmentTvShowApiBinding
import com.example.android_imperative.model.TVShow
import com.example.android_imperative.utils.Logger
import com.example.android_imperative.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TVShowApiFragment : Fragment() {
    private val TAG = "TAG" + this::class.simpleName.toString()
    private var _binding: FragmentTvShowApiBinding? = null
    private val binding get() = _binding!!

    val viewModel: MainViewModel by viewModels()
    private val adapter by lazy { TVShowAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowApiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        initObserves()
        val lm = GridLayoutManager(requireContext(), 2)
        binding.rvHome.layoutManager = lm
        viewModel.apiTvShowPopular(1)
        binding.rvHome.adapter = adapter

        binding.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (lm.findLastCompletelyVisibleItemPosition() == adapter.items.size - 1) {
                    val nextPage = viewModel.tvShowPopular.value!!.page + 1
                    val totalPage = viewModel.tvShowPopular.value!!.pages
                    if (nextPage <= totalPage) {
                        Logger.d(TAG, nextPage.toString())
                        viewModel.apiTvShowPopular(nextPage)
                    }
                }
            }
        })

        binding.bFab.setOnClickListener {
            binding.rvHome.smoothScrollToPosition(0)
        }

        adapter.onImageClick = { tvShow, ivMovie ->
            viewModel.insertTVShowToDB(tvShow)
            callDetailsActivity(tvShow, ivMovie)
        }
    }

    private fun initObserves() {
        // Retrofit Related
        viewModel.tvShowsFromApi.observe(requireActivity()) {
            Logger.d(TAG, it.size.toString())
            adapter.submitList(it)
        }
        viewModel.errorMessage.observe(requireActivity()) {
            Logger.d(TAG, it.toString())
        }
        viewModel.isLoading.observe(requireActivity()) {
            Logger.d(TAG, it.toString())
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun callDetailsActivity(tvShow: TVShow, sharedImageView: ImageView) {
        val intent = Intent(requireContext(), DetailsActivity::class.java)
        intent.putExtra("show_id", tvShow.id)
        intent.putExtra("show_img", tvShow.image_thumbnail_path)
        intent.putExtra("show_name", tvShow.name)
        intent.putExtra("show_network", tvShow.network)
        intent.putExtra("show_movie", ViewCompat.getTransitionName(sharedImageView))
        Log.d(TAG, "callDetailsActivity: $sharedImageView")

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(), sharedImageView, ViewCompat.getTransitionName(sharedImageView)!!
        )
        startActivity(intent, options.toBundle())
    }

}