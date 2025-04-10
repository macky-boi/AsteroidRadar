package com.example.asteroidradar.list

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.asteroidradar.R
import com.example.asteroidradar.data.remote.PictureOfTheDay

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, pictureOfTheDay: PictureOfTheDay?) {
    pictureOfTheDay?.let {
        val imgUri = it.url.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(null)
                .error(null))
            .into(imgView)
    } ?: run {
        Glide.with(imgView.context)
            .load(R.drawable.no_internet_connection)
            .into(imgView)
    }
}