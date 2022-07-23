package com.example.android_imperative.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_imperative.databinding.ItemTvShowBinding
import com.example.android_imperative.model.TVShow

class TVShowAdapter : RecyclerView.Adapter<TVShowAdapter.ViewHolder>() {

    private val diff = AsyncListDiffer(this, ITEM_DIFF)
    val items: ArrayList<TVShow> = ArrayList()
    var onImageClick: ((TVShow, ImageView) -> Unit)? = null
    var onRootClick: ((Long, TVShow) -> Unit)? = null

    inner class ViewHolder(private val binding: ItemTvShowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val tvShow = diff.currentList[adapterPosition]
            binding.apply {
                ViewCompat.setTransitionName(ivMovie, tvShow.name)
                Glide.with(binding.root).load(tvShow.image_thumbnail_path).into(ivMovie)
                tvName.text = tvShow.name
                tvType.text = tvShow.network

                ivMovie.setOnClickListener {
                    onImageClick?.invoke(tvShow, ivMovie)
                }
                ivMovie.setOnLongClickListener {
                    onRootClick?.invoke(tvShow.id, tvShow)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTvShowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind()
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    fun submitList(newItems: ArrayList<TVShow>) {
        items.addAll(newItems)
        diff.submitList(items)
        notifyDataSetChanged()
    }

    fun removeItem(item: TVShow){
        items.remove(item)
        diff.submitList(items)
        notifyDataSetChanged()
    }

    companion object {
        val ITEM_DIFF = object : DiffUtil.ItemCallback<TVShow>() {
            override fun areItemsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
                return oldItem.name == newItem.name
                        && oldItem.image_thumbnail_path == newItem.image_thumbnail_path
                        && oldItem.network == newItem.network
                        && oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
                return oldItem == newItem
            }
        }
    }

}