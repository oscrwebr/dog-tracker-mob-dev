package com.example.mob_dev_portfolio.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.DatePickerDialog
import androidx.lifecycle.lifecycleScope
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.database.Pet
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.databinding.FragmentAddPetBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


class AddPetFragment : Fragment() {
    private lateinit var binding: FragmentAddPetBinding
    private val db by lazy { context.let { PetAppDatabase.getDatabase(it!!) } }

    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var datePickerDialog: DatePickerDialog

    private var selectedImage: Uri? = null
    private var selectedImagePath: String? = null
    // Code adapted from: https://developer.android.com/training/data-storage/shared/photopicker
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            selectedImage = uri

            selectedImagePath = saveSelectedImage(uri)
            val localUri = Uri.fromFile(File(selectedImagePath))
            binding.profilePictureSelect.setImageURI(uri)
        } else {
            Toast.makeText(requireContext(),
                getString(R.string.no_pictures_selected), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker()
        cancelButton()
        addButton()
        profilePictureHandler()
    }

    private fun profilePictureHandler() {
        binding.chooseImageButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun saveSelectedImage(uri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val fileName = "pet_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }

    private fun datePicker() {
        binding.dobEt.setOnClickListener{
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)
            val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

            datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, day ->
                val selectedDate = "$day/${month + 1}/$year"
                binding.dobEt.setText(selectedDate)
            }, currentYear, currentMonth, currentDay)
            datePickerDialog.show()

        }
    }

    private fun cancelButton() {
        binding.cancelBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun addButton() {
        binding.addBtn.setOnClickListener {
            val name = binding.nameEt.text.toString()
            val petType = binding.petTypeEt.text.toString()
            val breed = binding.breedEt.text.toString()
            val dateOfBirth = binding.dobEt.text.toString()
            val gender = binding.genderEt.text.toString()
            val profilePicture = selectedImagePath


            if (name.isNotEmpty() && petType.isNotEmpty() && breed.isNotEmpty() && dateOfBirth.isNotEmpty() && gender.isNotEmpty()) {
                val newPet = Pet(0, name, petType, breed, dateOfBirth, gender, profilePicture)
                insertPet(newPet)
            }
            else {
                Toast.makeText(requireContext(), getString(R.string.please_fill_in_all_fields), Toast.LENGTH_SHORT).show()
            }
        }

    }
    // Coroutine learning: https://developer.android.com/kotlin/coroutines
    private fun insertPet(pet: Pet) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.petDao().addPet(pet)
            }
            // Toast learning: https://developer.android.com/reference/android/widget/Toast?hl=en
            Toast.makeText(requireContext(), getString(R.string.pet_added), Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

}