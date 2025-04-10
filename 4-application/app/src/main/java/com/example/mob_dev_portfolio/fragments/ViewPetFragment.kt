package com.example.mob_dev_portfolio.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.database.Pet
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.databinding.FragmentViewPetBinding
import com.example.mob_dev_portfolio.fragments.PetFragment.Companion.PET_SELECTED
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
        backButton()
        viewPetFragmentHandler()
    }

    private fun viewPetFragmentHandler() {
        val petArguments = arguments?.getSerializable(PET_SELECTED, Pet::class.java)
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

    private fun backButton() {
        binding.backFab.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}