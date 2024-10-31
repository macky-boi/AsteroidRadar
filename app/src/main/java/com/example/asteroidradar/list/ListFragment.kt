package com.example.asteroidradar.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.asteroidradar.databinding.FragmentListBinding

class ListFragment: Fragment() {

//    private val viewModel: ListingViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("ListingFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("ListingFragment", "onCreateView")

        val binding = FragmentListBinding.inflate(inflater)

        return binding.root
    }
}