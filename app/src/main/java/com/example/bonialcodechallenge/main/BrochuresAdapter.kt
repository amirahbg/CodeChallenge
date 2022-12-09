package com.example.bonialcodechallenge.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.bonialcodechallenge.R
import com.example.bonialcodechallenge.data.models.Brochure
import com.example.bonialcodechallenge.databinding.ItemBrochureBinding

class BrochuresAdapter : ListAdapter<Brochure, BrochuresAdapter.BrochureViewHolder>(BrochureDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrochureViewHolder {
        val binding = ItemBrochureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrochureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrochureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class BrochureDiffUtil : DiffUtil.ItemCallback<Brochure>() {
        override fun areItemsTheSame(oldItem: Brochure, newItem: Brochure) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Brochure, newItem: Brochure) = oldItem == newItem
    }

    inner class BrochureViewHolder(private val binding: ItemBrochureBinding) : ViewHolder(binding.root) {
        fun bind(brochure: Brochure) {
            binding.retailerNameText.text = brochure.retailerName
            Glide.with(binding.brochureImage)
                .load(brochure.brochureImageUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_broken_image_24)
                .into(binding.brochureImage)
        }
    }
}