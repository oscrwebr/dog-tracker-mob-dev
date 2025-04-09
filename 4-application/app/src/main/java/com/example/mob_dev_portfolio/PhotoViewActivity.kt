package com.example.mob_dev_portfolio

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.database.Photo
import com.example.mob_dev_portfolio.databinding.ActivityPhotoViewBinding
import com.example.mob_dev_portfolio.fragments.GalleryFragment
import com.example.mob_dev_portfolio.fragments.PetFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PhotoViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPhotoViewBinding
    private val db by lazy { PetAppDatabase.getDatabase(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        photoViewActivityHandler()
        floatingButtonHandler()
        changeNameButtonHandler()
        cancelButtonHandler()
        submitButtonHandler()



    }

    private fun photoViewActivityHandler() {
        // Intent tutorial partly based on: https://www.geeksforgeeks.org/how-to-apply-onclicklistener-to-recyclerview-items-in-android/
        val photoExtra = intent.getSerializableExtra(GalleryFragment.NEXT_SCREEN) as? Photo

        // Reference to my PhotoAdapter image URI setter
        if (photoExtra!=null) {
            val file = File(photoExtra.photo)
            binding.photoView.setImageURI(Uri.fromFile(file))
            if (photoExtra.name != null) {
                binding.photoViewText.text = photoExtra.name
            }
        } else {
            binding.photoView.setImageResource(R.drawable.photo_library)
        }
    }

    private fun floatingButtonHandler() {
        binding.floatingActionButton2.setOnClickListener {
            finish()
        }
    }

    private fun submitButtonHandler() {
        binding.submitButton.setOnClickListener {
            val photoName = binding.editPhotoName.text.toString()

            if (photoName.isNotEmpty()) {
                insertPhotoName(photoName)
                binding.popUp.visibility = View.GONE
                binding.editPhotoName.text.clear()
                binding.photoViewText.text = photoName
            }
        }
    }

    private fun insertPhotoName(photoName: String) {
        val photoExtra = intent.getSerializableExtra(GalleryFragment.NEXT_SCREEN) as Photo
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.photoDao().changeName(photoName, photoExtra.photoId)
            }
        }
    }

    private fun changeNameButtonHandler() {
        binding.changeNameButton.setOnClickListener {
            binding.popUp.visibility = View.VISIBLE
        }
    }

    private fun cancelButtonHandler () {
        binding.cancelButton.setOnClickListener {
            binding.popUp.visibility = View.GONE
        }
    }




}