package com.example.asteroidradar.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.asteroidradar.R

@Composable
fun KeyValueText(
    key: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row (
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
    ){
        Text(
            text = "$key: ",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.card_text_vertical_space))
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = dimensionResource(id = R.dimen.card_text_vertical_space))
        )
    }
}