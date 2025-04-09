package com.example.asteroidradar.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.asteroidradar.databinding.FragmentDetailBinding
import com.example.asteroidradar.model.Asteroid
import com.example.asteroidradar.AsteroidAppViewModel
import com.example.asteroidradar.list.ListFragmentDirections

class DetailFragment: Fragment() {

    private val viewModel: AsteroidAppViewModel by activityViewModels { AsteroidAppViewModel.Factory }
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = binding.root.findNavController()

        binding.apply {
            viewModelBinding = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.currentAsteroidEntity.observe(viewLifecycleOwner) { asteroid ->
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }

        viewModel.navigateToList.observe(viewLifecycleOwner) {
            navController.navigate(DetailFragmentDirections.actionDetailFragmentToList())
        }
    }
}