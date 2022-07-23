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
import com.example.android_imperative.databinding.FragmentTvShowLocalBinding
import com.example.android_imperative.model.TVShow
import com.example.android_imperative.utils.Logger
import com.example.android_imperative.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TVShowLocalFragment : Fragment() {
    private val TAG = "TAG" + this::class.simpleName.toString()
    private var _binding: FragmentTvShowLocalBinding? = null
    private val binding get() = _binding!!

    val viewModel: MainViewModel by viewModels()
    private val adapter by lazy { TVShowAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTvShowLocalBinding.inflate(inflater, container, false)
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
        binding.rvHome.layoutManager = GridLayoutManager(requireContext(), 2)
        viewModel.getTVShowsFromDB()
        binding.rvHome.adapter = adapter

        binding.bFab.setOnClickListener {
            binding.rvHome.smoothScrollToPosition(0)
        }

        adapter.onImageClick = { tvShow, ivMovie ->
            callDetailsActivity(tvShow, ivMovie)
        }

        adapter.onRootClick = { id, tvShow ->
            val bottomSheetFragment = BottomSheetFragmentYesNo("Do you want to delete this TVShow?")
            bottomSheetFragment.show((requireActivity()).supportFragmentManager,"BottomSheetfragmentLogOut")

            bottomSheetFragment.clickYes = {
                viewModel.removeTVShowFromDB(id)
                adapter.removeItem(tvShow)
                bottomSheetFragment.dismiss()
            }
        }
    }

    private fun initObserves() {
        // Room Related
        viewModel.tvShowsFromDB.observe(requireActivity()) {
            Logger.d(TAG, it.size.toString())
            adapter.submitList(it)
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