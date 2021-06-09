package com.github.nthily.flappybird

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource


@Composable
fun Background(){
    Image(painterResource(id = R.drawable.bkg), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
}