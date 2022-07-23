package com.example.android_imperative.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android_imperative.databinding.ItemTvShortBinding

class TVShortAdapter : RecyclerView.Adapter<TVShortAdapter.ViewHolder>() {

    private val diff = AsyncListDiffer(this, ITEM_DIFF)

    inner class ViewHolder(private val binding: ItemTvShortBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val tvShow = diff.currentList[adapterPosition]
            binding.apply {
                Glide.with(binding.root).load(tvShow).into(ivShort)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemTvShortBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind()
    }

    override fun getItemCount(): Int {
        return diff.currentList.size
    }

    fun submitList(items: List<String>) {
        diff.submitList(items)
    }

    companion object {
        val ITEM_DIFF = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }

}