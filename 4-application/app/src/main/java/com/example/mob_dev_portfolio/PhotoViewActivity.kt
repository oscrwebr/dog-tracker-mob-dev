package com.example.mob_dev_portfolio

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mob_dev_portfolio.database.Photo
import com.example.mob_dev_portfolio.databinding.ActivityPhotoViewBinding
import com.example.mob_dev_portfolio.fragments.GalleryFragment
import com.example.mob_dev_portfolio.fragments.PetFragment
import java.io.File

class PhotoViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPhotoViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        photoViewActivityHandler()
        floatingButtonHandler()



    }

    private fun photoViewActivityHandler() {
        val photoExtra = intent.getStringExtra(GalleryFragment.NEXT_SCREEN)

        // Reference to my PhotoAdapter image URI setter
        if (!photoExtra.isNullOrEmpty()){
            val file = File(photoExtra)
            binding.photoView.setImageURI(Uri.fromFile(file))
        } else {
            binding.photoView.setImageResource(R.drawable.photo_library)
        }
    }

    private fun floatingButtonHandler() {
        binding.floatingActionButton2.setOnClickListener {
            finish()
        }
    }




}