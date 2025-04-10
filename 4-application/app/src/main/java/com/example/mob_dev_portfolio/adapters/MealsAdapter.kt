package com.example.mob_dev_portfolio.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.adapters.WalksAdapter.WalksViewHolder
import com.example.mob_dev_portfolio.database.Meal
import com.example.mob_dev_portfolio.database.Pet
import com.example.mob_dev_portfolio.database.Walk
import com.example.mob_dev_portfolio.databinding.MealsRowBinding
import com.example.mob_dev_portfolio.databinding.WalksRowBinding
import java.io.File


// Same implementation as my WalksAdapter class, just slightly tweaked for meals (removal of onClickListener)
class MealsAdapter(private var mealsList: List<Meal>, private var petsList: List<Pet>): RecyclerView.Adapter<MealsAdapter.MealsViewHolder>() {

    class MealsViewHolder(val binding: MealsRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(meal: Meal, pet: Pet) {
            binding.mealName.text = meal.mealName
            binding.mealDate.text = meal.mealDate
            binding.mealTime.text = meal.mealTime

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder {
        val binding: MealsRowBinding = MealsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MealsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }



    fun updateMeals(newMeals: List<Meal>, newPets: List<Pet>) {
        this.mealsList = newMeals
        this.petsList = newPets
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        val currentItem = mealsList[position]
        val currentPet = petsList.find { pet -> pet.id == currentItem.petId }
        holder.bind(currentItem, currentPet!!)
    }
}