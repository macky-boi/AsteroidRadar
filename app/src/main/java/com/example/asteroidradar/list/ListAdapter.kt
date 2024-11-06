package com.example.asteroidradar.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.asteroidradar.R
import com.example.asteroidradar.data.local.asteroid.AsteroidEntity
import com.example.asteroidradar.data.local.pictureOfTheDay.PictureOfTheDay
import com.example.asteroidradar.databinding.ItemAsteroidBinding
import com.example.asteroidradar.databinding.PictureOfTheDayItemBinding

class ListAdapter(private val clickListener: AsteroidListener) : ListAdapter<AsteroidEntity,
        RecyclerView.ViewHolder>(ListDiffCallback()) {

    var pictureOfTheDay: PictureOfTheDay? = null
        set(value) {
            field = value
            notifyItemChanged(0) // Update header whenever picture changes
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            // Bind the header (PictureOfTheDay)
            if (holder is PictureOfTheDayViewHolder) {
                holder.bind(pictureOfTheDay)
            }
        } else {
            // Subtract 1 from the position only for the asteroids (because position 0 is the header)
            val asteroidPosition = position - 1
            val asteroid = getItem(asteroidPosition)
            if (holder is AsteroidViewHolder) {
                holder.bind(clickListener, asteroid)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            PictureOfTheDayViewHolder.from(parent)
        } else {
            AsteroidViewHolder.from(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        // If the position is 0, it's the header
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    class AsteroidViewHolder private constructor(private val binding: ItemAsteroidBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AsteroidListener, item: AsteroidEntity) {
            binding.asteroid = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAsteroidBinding.inflate(layoutInflater, parent, false)

                return AsteroidViewHolder(binding)
            }
        }
    }

    class PictureOfTheDayViewHolder private constructor(private val binding: PictureOfTheDayItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(pictureOfTheDay: PictureOfTheDay?) {
            pictureOfTheDay?.let {
                Log.i("PictureOfTheDayViewHolder", "pictureOfTheDay not null | pictureOfTheDay: $it")

                val imgUri = it.url.toUri().buildUpon().scheme("https").build()
                Log.i("PictureOfTheDayViewHolder", "imgUri: $imgUri")

                Glide.with(binding.imageView.context)
                    .load(imgUri)
                    .into(binding.imageView)

            } ?: run {
                Log.i("PictureOfTheDayViewHolder", "pictureOfTheDay null | pictureOfTheDay: $pictureOfTheDay")
                Glide.with(binding.imageView.context)
                    .load(R.drawable.no_internet_connection)
                    .into(binding.imageView)
            }
        }

        companion object {
            fun from(parent: ViewGroup): PictureOfTheDayViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PictureOfTheDayItemBinding.inflate(layoutInflater, parent, false)
                return PictureOfTheDayViewHolder(binding)
            }
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class ListDiffCallback : DiffUtil.ItemCallback<AsteroidEntity>() {
    override fun areItemsTheSame(oldItem: AsteroidEntity, newItem: AsteroidEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AsteroidEntity, newItem: AsteroidEntity): Boolean {
        return oldItem == newItem
    }
}

class AsteroidListener(val clickListener: (asteroidEntity: AsteroidEntity) -> Unit) {
    fun onClick(asteroidEntity: AsteroidEntity) = clickListener(asteroidEntity)
}