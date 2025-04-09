package com.example.mob_dev_portfolio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mob_dev_portfolio.databinding.ActivityMainBinding
import com.example.mob_dev_portfolio.fragments.WalksFragment
import com.example.mob_dev_portfolio.fragments.MealsFragment
import com.example.mob_dev_portfolio.fragments.GalleryFragment
import com.example.mob_dev_portfolio.fragments.PetFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (savedInstanceState == null){
            replaceFragment(PetFragment())
        }

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.pets -> replaceFragment(PetFragment())
                R.id.walks -> replaceFragment(WalksFragment())
                R.id.meals -> replaceFragment(MealsFragment())
                R.id.gallery -> replaceFragment(GalleryFragment())

                else -> {
                }
            }
            true
        }
    }


    // Code adapted from: https://www.youtube.com/watch?v=L_6poZGNXOo
    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(R.id.fragmentContainerView, fragment)
        fragmentManager.commit()
    }
}