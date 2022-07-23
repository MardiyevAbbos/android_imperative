package com.example.android_imperative.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_imperative.databinding.ItemTvEpisodeBinding
import com.example.android_imperative.model.Episode

class TVEpisodeAdapter : ListAdapter<Episode, TVEpisodeAdapter.ViewHolder>(ITEM_DIFF) {

    inner class ViewHolder(private val binding: ItemTvEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind() {
            val episode = getItem(adapterPosition)
            with(binding){
                tvEpisode.text = "Episode: ${episode.episode},"
                tvSeason.text = "Season: ${episode.season}."
                tvName.text = "Name: ${episode.name}"
                tvAirDate.text = episode.air_date
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTvEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind()
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    companion object {
        val ITEM_DIFF = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem.name == newItem.name
                        && oldItem.episode == newItem.episode
                        && oldItem.air_date == newItem.air_date
                        && oldItem.season == newItem.season
            }
            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
                return oldItem == newItem
            }
        }
    }

}