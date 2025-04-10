package com.example.mob_dev_portfolio.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.database.Pet
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.databinding.FragmentViewPetBinding
import com.example.mob_dev_portfolio.fragments.PetFragment.Companion.PET_SELECTED
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class ViewPetFragment : Fragment() {
    private lateinit var binding: FragmentViewPetBinding
    private val db by lazy { context.let { PetAppDatabase.getDatabase(it!!) } }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val petArguments = arguments?.getSerializable(PET_SELECTED, Pet::class.java)
        val petId = petArguments?.id
        backButton()
        viewPetFragmentHandler()
        deleteButtonHandler()
    }

    // Setting text views & profile picture with the argument bundle we sent from PetFragment
    private fun viewPetFragmentHandler() {
        val petArguments = arguments?.getSerializable(PET_SELECTED, Pet::class.java)
        val petId = petArguments?.id
        if (petArguments != null) {
            if(!petArguments.profilePicture.isNullOrBlank()) {
                val file = File(petArguments.profilePicture)
                if(file.exists()) {
                    binding.profileImage.setImageURI(Uri.fromFile(file))
                } else {
                    binding.profileImage.setImageResource(R.drawable.profile_picture)
                }
            } else {
                binding.profileImage.setImageResource(R.drawable.profile_picture)
            }
            binding.petNameView.text = petArguments.name
            binding.petTypeView.text = petArguments.petType
            binding.dateOfBirthView.text = petArguments.dateOfBirth
            binding.genderView.text = petArguments.gender
        }
    }

    private fun deleteButtonHandler() {
        val petArguments = arguments?.getSerializable(PET_SELECTED, Pet::class.java)
        val petId = petArguments?.id
        binding.deleteButton.setOnClickListener{
            if(petId != null) {
                deletePet(petId)
                Toast.makeText(context, getString(R.string.pet_deleted), Toast.LENGTH_SHORT).show()
            }
            parentFragmentManager.popBackStack()



        }
    }

    private fun deletePet(petId: Int) {
        lifecycleScope.launch{
            withContext(Dispatchers.IO) {
                db.petDao().deletePet(petId)
            }
        }

    }
    // Goes back to previous fragment
    private fun backButton() {
        binding.backFab.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}