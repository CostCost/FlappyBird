package com.github.nthily.flappybird.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp


class GameObject {

    val jumpDistance = 130f

    var limitHeight by mutableStateOf(0.dp)
    var limitWidth by mutableStateOf(0.dp)

}