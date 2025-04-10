package com.example.mob_dev_portfolio.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.database.Pet
import com.example.mob_dev_portfolio.database.Walk
import com.example.mob_dev_portfolio.databinding.WalksRowBinding
import java.io.File

class WalksAdapter(private var walksList: List<Walk>, private var petsList: List<Pet>, private var onClickListener: OnClickListener? = null): RecyclerView.Adapter<WalksAdapter.WalksViewHolder>() {

    class WalksViewHolder(val binding: WalksRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(walk: Walk, pet: Pet) {
            binding.walkName.text = walk.walkName
            binding.walkDate.text = walk.date
            binding.walkTime.text = walk.time

            // Image URI binder from PetAdapter class
            // Check if profile picture isn't null in database (user hasn't given a profile picture)
            if(!pet.profilePicture.isNullOrBlank()) {
                val file = File(pet.profilePicture)
                if(file.exists()) {
                    // Setting image URI for the profile picture image view
                    binding.profileImage.setImageURI(Uri.fromFile(file))
                } else {
                    // Otherwise give a default drawable image
                    binding.profileImage.setImageResource(R.drawable.profile_picture)
                }
            } else {
                // Otherwise give a default drawable image
                binding.profileImage.setImageResource(R.drawable.profile_picture)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalksViewHolder {
        val binding: WalksRowBinding = WalksRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalksViewHolder(binding)
    }
    fun setOnClickListener(onClickListener: com.example.mob_dev_portfolio.adapters.WalksAdapter.OnClickListener) {
        this.onClickListener = onClickListener
    }
    // RecyclerView OnClickListener adapted from: https://www.geeksforgeeks.org/how-to-apply-onclicklistener-to-recyclerview-items-in-android/
    interface OnClickListener {
        fun onClick(position: Int, model: Walk)
    }

    fun updateWalks(newWalks: List<Walk>, newPets: List<Pet>) {
        this.walksList = newWalks
        this.petsList = newPets
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return walksList.size
    }

    override fun onBindViewHolder(holder: WalksViewHolder, position: Int) {
        val currentItem = walksList[position]
        val currentPet = petsList.find { pet -> pet.id == currentItem.petId }
        holder.bind(currentItem, currentPet!!)
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, currentItem)
        }
    }
}