package com.example.asteroidradar.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.asteroidradar.databinding.FragmentDetailBinding
import com.example.asteroidradar.databinding.FragmentListBinding
import com.example.asteroidradar.ui.AsteroidAppViewModel

class DetailFragment: Fragment() {

//    private lateinit var viewModel: AsteroidAppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("DetailFragment", "onCreate")

//        viewModel = ViewModelProvider(this, AsteroidAppViewModel.Factory
//        )[AsteroidAppViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("DetailFragment", "onCreateView")

        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.viewModel = viewModel

//        viewModel.asteroids.observe(viewLifecycleOwner, Observer { asteroids ->
//            Log.i("DetailFragment", "asteroids: $asteroids")
//            asteroids?.let {
//                adapter.submitList(asteroids)
//            }
//        })

        return binding.root
    }
}