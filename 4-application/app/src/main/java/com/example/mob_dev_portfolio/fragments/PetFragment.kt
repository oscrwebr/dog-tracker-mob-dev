package com.example.mob_dev_portfolio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.databinding.FragmentPetBinding
import com.example.mob_dev_portfolio.adapters.PetAdapter
import com.example.mob_dev_portfolio.database.Pet

class PetFragment : Fragment() {

    private lateinit var binding: FragmentPetBinding
    private val db by lazy { context.let { PetAppDatabase.getDatabase(it!!) } }
    private lateinit var petAdapter: PetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        floatingButtonHandler(AddPetFragment())
        recyclerViewHandler()
    }

    private fun floatingButtonHandler(fragment: Fragment) {
        binding.floatingActionButton.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace(R.id.fragmentContainerView,fragment)
            fragmentManager.addToBackStack(null)
            fragmentManager.commit()
        }
    }

    private fun recyclerViewHandler() {
        petAdapter = PetAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerView.adapter = petAdapter

        db.petDao().getPets().observe(viewLifecycleOwner, Observer { pets ->
            petAdapter.updatePets(pets)
        })

        // Bundle learning: https://stackoverflow.com/questions/4999991/what-is-a-bundle-in-an-android-application
        petAdapter.setOnClickListener(object: PetAdapter.OnClickListener {
            override fun onClick(position: Int, model: Pet) {
                val bundle = Bundle()
                bundle.putSerializable(PET_SELECTED, model)
                val viewPetFragment = ViewPetFragment()
                viewPetFragment.arguments = bundle
                val fragmentManager = parentFragmentManager.beginTransaction()
                fragmentManager.replace(R.id.fragmentContainerView, viewPetFragment)
                fragmentManager.addToBackStack(null)
                fragmentManager.commit()
            }
        })
    }

    companion object {
        val PET_SELECTED = "pet_selected"
    }


}