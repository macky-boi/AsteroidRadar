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
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = ListAdapter(AsteroidListener { asteroid ->
            viewModel.updateCurrentAsteroid(asteroid)
            viewModel.navigateToDetailPage()
        })

        binding.asteroidList.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner, Observer { asteroids ->
            adapter.submitList(asteroids)
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate) {
                val navController = binding.root.findNavController()
                navController.navigate(ListFragmentDirections.actionListToDetailFragment())
                viewModel.navigatedToDetailPage()
            }
        })

        viewModel.pictureOfTheDay.observe(viewLifecycleOwner, Observer { pictureOfTheDay ->
            Log.i("ListingFragment", "setting adapter pictureOfTheDay: $pictureOfTheDay")
            adapter.pictureOfTheDay = pictureOfTheDay
        })

        return binding.root
    }
}