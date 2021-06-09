package com.github.nthily.flappybird

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UiState @Inject constructor()
    : ViewModel() {

    var gameing by mutableStateOf(false)

    var jumpHeight by mutableStateOf(0.dp)

    var maxHeight by mutableStateOf(0.dp)

    var downToTheBottomValue by mutableStateOf(1f)

    var flying by mutableStateOf(false)
}