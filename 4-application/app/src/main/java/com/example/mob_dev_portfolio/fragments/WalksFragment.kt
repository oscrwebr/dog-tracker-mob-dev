package com.example.mob_dev_portfolio.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mob_dev_portfolio.AddWalkActivity
import com.example.mob_dev_portfolio.R
import com.example.mob_dev_portfolio.databinding.FragmentWalksBinding

class WalksFragment : Fragment() {
    private lateinit var binding: FragmentWalksBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        floatingButtonHandler()
    }

    private fun floatingButtonHandler() {
        binding.addWalksFab.setOnClickListener{
            val intent = Intent(requireContext(), AddWalkActivity::class.java)
            startActivity(intent)
        }
    }


}