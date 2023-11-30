package com.example.jsondatademo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.jsondatademo.databinding.CustomLayoutBinding
import com.squareup.picasso.Picasso

class SubCategoryAdapter(private val subCategoryList: MutableList<SubCategoryDetails>) :
    RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder>() {

// Preparing this function for single recyclerview in my app,for online as well as offline
    fun updateData(updatedData: List<SubCategoryDetails>) {
        subCategoryList.clear()
        subCategoryList.addAll(updatedData)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CustomLayoutBinding.inflate(inflater, parent, false)
        return SubCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {
        val subCategory = subCategoryList[position]


        // Load image using Picasso or any other image loading library
        Picasso.get().load(subCategory.bannerImage).into(holder.binding.appImageView)
        holder.binding.appNameTextView.text = subCategory.name
        // Bind the data to your views in the ViewHolder
        holder.bind(subCategory)
    }

    override fun getItemCount(): Int = subCategoryList.size

    inner class SubCategoryViewHolder(val binding: CustomLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Initialize your views from itemView

        fun bind(subCategory: SubCategoryDetails) {
            // Bind subcategory data to your views
        }
    }
}
