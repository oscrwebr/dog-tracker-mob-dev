package com.example.mob_dev_portfolio.fragments

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mob_dev_portfolio.AddMealActivity
import com.example.mob_dev_portfolio.AddWalkActivity
import com.example.mob_dev_portfolio.ViewWalkActivity
import com.example.mob_dev_portfolio.adapters.MealsAdapter
import com.example.mob_dev_portfolio.adapters.WalksAdapter
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.database.Walk
import com.example.mob_dev_portfolio.databinding.FragmentMealsBinding
import com.example.mob_dev_portfolio.fragments.WalksFragment.Companion.WALK_ROUTE

class MealsFragment : Fragment() {
    private lateinit var binding: FragmentMealsBinding
    private lateinit var mealsAdapter: MealsAdapter
    private val db by lazy { context.let { PetAppDatabase.getDatabase(it!!) } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapterHandler()
        showTodayMealsAutomatically()
        floatingButtonHandler()
        calendarClickHandler()


    }

    // Code largely taken from my WalksFragment class, tweaked to fit meals
    // You can check references there
    private fun floatingButtonHandler() {
        binding.addMealsFab.setOnClickListener{
            val intent = Intent(requireContext(), AddMealActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showTodayMealsAutomatically() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val currentDay = "$day/${month + 1}/$year"
        recyclerViewHandler(currentDay)
    }

    private fun adapterHandler() {
        mealsAdapter = MealsAdapter(emptyList(), emptyList())
        binding.mealsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.mealsRecyclerView.adapter = mealsAdapter

    }

    // Fetches the date of the current date selected in the Calendar View, and performs the recyclerViewHandler function
    // Code adapted from: https://stackoverflow.com/questions/16031314/how-can-i-get-selected-date-in-calenderview-in-android
    // Also implemented in my WalksFragment class
    private fun calendarClickHandler() {
        binding.calendarViewMeals.setOnDateChangeListener { _, year: Int, month: Int, day: Int ->
            val selectedDate = "$day/${month + 1}/$year"
            recyclerViewHandler(selectedDate)
        }
    }


    // Updates the recycler view with the walks on the selected date
    private fun recyclerViewHandler(selectedDate: String) {
        db.petDao().getPets().observe(viewLifecycleOwner) { pets ->
            db.mealDao().getMealsOnDate(selectedDate).observe(viewLifecycleOwner) { meals ->
                mealsAdapter.updateMeals(meals, pets)
            }
        }
    }
}