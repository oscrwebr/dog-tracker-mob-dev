package com.example.mob_dev_portfolio.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.database.Photo
import com.example.mob_dev_portfolio.databinding.GalleryRowBinding
import java.io.File

class PhotoAdapter(private var photoList: List<Photo>, private var onClickListener: OnClickListener? = null): RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(val binding: GalleryRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            val file = File(photo.photo)
            binding.photo.setImageURI(Uri.fromFile(file))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = GalleryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding)
    }

    // Setter for the click listener
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    // RecyclerView OnClickListener adapted from: https://www.geeksforgeeks.org/how-to-apply-onclicklistener-to-recyclerview-items-in-android/
    interface OnClickListener {
        fun onClick(position: Int, model: Photo)
    }

    override fun getItemCount() = photoList.size

    fun updatePhotos(newPhotos: List<Photo>) {
        this.photoList = newPhotos
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val currentItem = photoList[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, currentItem)
        }

    }
}