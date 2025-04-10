package com.example.mob_dev_portfolio.fragments

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mob_dev_portfolio.AddWalkActivity
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.adapters.WalksAdapter
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.databinding.FragmentWalksBinding

class WalksFragment : Fragment() {
    private lateinit var binding: FragmentWalksBinding
    private val db by lazy { context.let { PetAppDatabase.getDatabase(it!!) } }
    private lateinit var walksAdapter: WalksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCurrentDayWalks()
        floatingButtonHandler()
        adapterHandler()
        calendarClickHandler()
    }

    // Function for the floating action button (takes us to the AddWalkActivity)
    private fun floatingButtonHandler() {
        binding.addWalksFab.setOnClickListener{
            val intent = Intent(requireContext(), AddWalkActivity::class.java)
            startActivity(intent)
        }
    }

    // Setting adapter for recycler view
    private fun adapterHandler() {
        walksAdapter = WalksAdapter(emptyList(), emptyList())
        binding.walksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.walksRecyclerView.adapter = walksAdapter
    }

    // Updates the recycler view with the walks on the selected date
    private fun recyclerViewHandler(selectedDate: String) {
        db.petDao().getPets().observe(viewLifecycleOwner) { pets ->
            db.walkDao().getWalksOnDate(selectedDate).observe(viewLifecycleOwner) { walks ->
                walksAdapter.updateWalks(walks, pets)
            }
        }
    }

//    private fun recyclerViewClickListener() {
//        walksAdapter.setOnClickListener
//    }

    // Fetches the date of the current date selected in the Calendar View, and performs the recyclerViewHandler function
    // Code adapted from: https://stackoverflow.com/questions/16031314/how-can-i-get-selected-date-in-calenderview-in-android
    private fun calendarClickHandler() {
        binding.calendarView.setOnDateChangeListener { _, year: Int, month: Int, day: Int ->

            val selectedDate = "$day/${month + 1}/$year"
            recyclerViewHandler(selectedDate)
        }
    }

    // Function to display today's walks on start of fragment, instead of having to click (because of how I set it up)
    private fun loadCurrentDayWalks() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val currentDay = "$day/${month + 1}/$year"
        recyclerViewHandler(currentDay)
    }




}