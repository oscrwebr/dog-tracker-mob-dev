package com.example.mob_dev_portfolio.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.database.Pet
import com.example.mob_dev_portfolio.databinding.PetRowBinding
import java.io.File

// RecyclerView adapter code adapted from: https://www.youtube.com/watch?v=QdrJg0MFtOE
class PetAdapter(private var petList: List<Pet>): RecyclerView.Adapter<PetAdapter.ViewHolder>() {

    class ViewHolder(val binding: PetRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pet: Pet) {
            binding.petName.text = pet.name
            binding.petBreed.text = pet.breed

            if (!pet.profilePicture.isNullOrBlank()) {
                val file = File(pet.profilePicture)
                if(file.exists()) {
                    binding.profileImage.setImageURI(Uri.fromFile(file))
                } else {
                    binding.profileImage.setImageResource(R.drawable.profile_picture)
                }
            } else {
                binding.profileImage.setImageResource(R.drawable.profile_picture)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PetRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = petList.size

    fun updatePets(newPets: List<Pet>) {
        this.petList = newPets
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = petList[position]
        holder.bind(currentItem)

    }


}