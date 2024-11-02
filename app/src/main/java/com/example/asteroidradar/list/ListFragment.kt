package com.example.asteroidradar.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.asteroidradar.databinding.FragmentListBinding
import com.example.asteroidradar.ui.AsteroidAppViewModel

class ListFragment: Fragment() {

    private lateinit var viewModel: AsteroidAppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("ListingFragment", "onCreate")

        viewModel = ViewModelProvider(this, AsteroidAppViewModel.Factory
        )[AsteroidAppViewModel::class.java]
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

        viewModel.isShowingListPage.observe(viewLifecycleOwner, Observer { isShowingListPage ->
            if (!isShowingListPage) {
                val navController = binding.root.findNavController()
                navController.navigate(ListFragmentDirections.actionListToDetailFragment())
            }
        })

        return binding.root
    }
}