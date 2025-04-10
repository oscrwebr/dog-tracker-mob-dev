package com.example.mob_dev_portfolio

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mob_dev_portfolio.database.Meal
import com.example.mob_dev_portfolio.database.Pet
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.database.Walk
import com.example.mob_dev_portfolio.databinding.ActivityAddMealBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Also adapted from my AddWalkActivity class (similar implementation just without the Google Maps API)
// You can find references for code in this class in the AddWalkActivity class
class AddMealActivity: AppCompatActivity() {
    private lateinit var binding: ActivityAddMealBinding
    private lateinit var datePickerDialog: DatePickerDialog
    private val db by lazy { PetAppDatabase.getDatabase(this) }
    private var petObjects: List<Pet> = emptyList()
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMealBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        backButtonHandler()
        spinnerHandler()
        datePicker()
        timePicker()
        addMealButtonHandler()

    }

    private fun backButtonHandler() {
        binding.backMealButton.setOnClickListener {
            finish()
        }
    }

    // Spinner handler partly based on: https://developer.android.com/develop/ui/views/components/spinner
    // Adapted to fetch from database
    private fun spinnerHandler() {
        db.petDao().getPets().observe(this) { pets ->

            petObjects = pets
            val petNames = pets.map { it.name }

            if (petNames.isEmpty()) {
                binding.noPetsInDatabase.visibility = android.view.View.VISIBLE
                binding.pleaseGoBack.visibility = android.view.View.VISIBLE
            } else {
                binding.noPetsInDatabase.visibility = android.view.View.GONE
                binding.pleaseGoBack.visibility = android.view.View.GONE


                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, petNames)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = adapter
            }
        }
    }

    // Function taken from my AddPetFragment class
    private fun datePicker() {
        binding.mealDateEt.setOnClickListener {
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(this, { _, year, month, day ->
                val selectedDate = "$day/${month + 1}/$year"
                binding.mealDateEt.setText(selectedDate)
            }, currentYear, currentMonth, currentDay)
            datePickerDialog.show()

        }
    }

    // Function taken from my AddWalkActivity class
    private fun timePicker() {
        binding.mealTimeEt.setOnClickListener{
            val hours = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(this, {_, hour, minute ->
                val selectedTime = "$hour:$minute"
                binding.mealTimeEt.setText(selectedTime)

            }, hours, minutes, true)
            timePickerDialog.show()
        }
    }

    // Taken from my AddWalkActivity class (without the markers)
    private fun insertMeal() {
        val selectedPetPosition = binding.spinner.selectedItemPosition
        val selectedPet = petObjects[selectedPetPosition]
        val selectedPetId = selectedPet.id

        val name = binding.mealNameEt.text.toString()
        val date = binding.mealDateEt.text.toString()
        val time = binding.mealTimeEt.text.toString()

        if (name.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val newMeal = Meal(0, selectedPetId, name, date, time)
                    db.mealDao().addMeal(newMeal)
                }

            }
        } else {
            Toast.makeText(
                this,
                getString(R.string.remember_to_fill_in_all_fields),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addMealButtonHandler() {
        binding.addMealButton.setOnClickListener {
            insertMeal()

            Toast.makeText(this, "Meal added!", Toast.LENGTH_SHORT).show()
        }
    }
}