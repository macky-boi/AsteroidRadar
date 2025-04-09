package com.example.asteroidradar.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.asteroidradar.databinding.FragmentListBinding
import com.example.asteroidradar.AsteroidAppViewModel

class ListFragment: Fragment() {

    private val viewModel: AsteroidAppViewModel by activityViewModels { AsteroidAppViewModel.Factory }
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = binding.root.findNavController()

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModelBinding = viewModel
        }

        viewModel.navigateToDetail.observe(viewLifecycleOwner) {
            navController.navigate(ListFragmentDirections.actionListToDetailFragment())
        }

        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        val adapter = ListAdapter(AsteroidListener { asteroid ->
            viewModel.updateCurrentAsteroid(asteroid)
            viewModel.navigateToDetailPage()
        })
        binding.asteroidList.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.pictureOfTheDay.observe(viewLifecycleOwner) {
            adapter.pictureOfTheDay = it
        }
    }
}