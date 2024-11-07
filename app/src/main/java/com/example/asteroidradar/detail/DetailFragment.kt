package com.example.asteroidradar.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.asteroidradar.databinding.FragmentDetailBinding
import com.example.asteroidradar.model.Asteroid
import com.example.asteroidradar.AsteroidAppViewModel

class DetailFragment: Fragment() {

    private val viewModel: AsteroidAppViewModel by activityViewModels { AsteroidAppViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("DetailFragment", "onCreate")
        Log.i("DetailFragment", "currentAsteroid: ${viewModel.currentAsteroidEntity.value}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("DetailFragment", "onCreateView")

        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        val emptyAsteroid = Asteroid(
            id = "",
            name = "",
            date = "",
            isHazardous = "",
            absoluteMagnitude = "",
            closeApproachDate = "",
            missDistanceAstronomical = "",
            relativeVelocityKilometersPerSecond = ""
        )

        viewModel.currentAsteroidEntity.observe(viewLifecycleOwner, Observer { asteroid ->
            Log.i("DetailFragment", "asteroid: $asteroid")
            binding.asteroid = asteroid?.toModel() ?: emptyAsteroid
            binding.executePendingBindings()
        })

        return binding.root
    }
}