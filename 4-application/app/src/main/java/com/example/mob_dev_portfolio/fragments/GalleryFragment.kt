package com.example.mob_dev_portfolio.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mob_dev_portfolio.PhotoViewActivity
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.database.PetAppDatabase
import com.example.mob_dev_portfolio.database.Photo
import com.example.mob_dev_portfolio.databinding.FragmentGalleryBinding
import com.example.mob_dev_portfolio.adapters.PhotoAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class GalleryFragment : Fragment() {
    private lateinit var binding: FragmentGalleryBinding
    private val db by lazy { context.let { PetAppDatabase.getDatabase(it!!) } }
    private lateinit var photoAdapter: PhotoAdapter

    // Code adapted from: https://developer.android.com/training/data-storage/shared/photopicker
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
        if (!uris.isNullOrEmpty()) {
            for (i in uris ) {
                val selectedImagePath = saveSelectedImage(i)

                val newPhoto = Photo(0, null, selectedImagePath)
                insertPhoto(newPhoto)
            }
            // Toast learning: https://developer.android.com/reference/android/widget/Toast?hl=en
            Toast.makeText(requireContext(), getString(R.string.photo_s_added), Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(requireContext(),
                getString(R.string.no_media_selected), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        galleryHandler()
    }

    private fun galleryHandler() {
        photoAdapter = PhotoAdapter(emptyList())
        binding.photoRecycler.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.photoRecycler.adapter = photoAdapter
        // PhotoPicker adapted from: https://developer.android.com/training/data-storage/shared/photopicker
        binding.galleryButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        // Code adapted from: https://www.geeksforgeeks.org/how-to-apply-onclicklistener-to-recyclerview-items-in-android/
        photoAdapter.setOnClickListener(object: PhotoAdapter.OnClickListener {
            override fun onClick(position: Int, model: Photo) {
                val intent = Intent(requireContext(),PhotoViewActivity::class.java)
                intent.putExtra(NEXT_SCREEN,model)
                startActivity(intent)
            }
        })

        db.photoDao().getPhotos().observe(viewLifecycleOwner, Observer { photos ->
            photoAdapter.updatePhotos(photos)
        })


    }

    companion object {
        val NEXT_SCREEN="photo_screen"
    }

    //
    private fun saveSelectedImage(uri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        val file = File(requireContext().filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }

    // Coroutine learning: https://developer.android.com/kotlin/coroutines
    private fun insertPhoto(photo: Photo) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.photoDao().addPhoto(photo)
            }
        }
    }


}