package com.example.asteroidradar.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.asteroidradar.databinding.FragmentDetailBinding
import com.example.asteroidradar.AsteroidAppViewModel
import com.example.asteroidradar.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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
            astronomicalUnitInfoBtn.setOnClickListener {
                showAlertDialog()
            }
            imageView.apply {
                if (viewModel.isCurrentAsteroidHazardous()) {
                    setImageResource(R.mipmap.hazardous_comet_img_foreground)
                    contentDescription = getString(R.string.hazardousComet_image_desc)
                } else {
                    setImageResource(R.mipmap.safe_comet_img_foreground)
                    contentDescription = getString(R.string.safeComet_image_desc)
                }
            }
        }

        viewModel.currentAsteroid.observe(viewLifecycleOwner) { asteroid ->
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }

        viewModel.navigateToList.observe(viewLifecycleOwner) {
            navController.navigate(DetailFragmentDirections.actionDetailFragmentToList())
        }
    }

    private fun showAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.au_info_message))
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}