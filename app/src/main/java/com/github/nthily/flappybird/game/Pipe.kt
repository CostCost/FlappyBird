package com.github.nthily.flappybird.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


class Pipe(
    pipeDownHeight: Dp,
    pipeUpHeight: Dp
){
    val width by mutableStateOf(60.dp)

    var pipeDownX by mutableStateOf(width.value)
    var pipeDownY by mutableStateOf(0f)
    var pipeDownHeight by mutableStateOf(pipeDownHeight)

    var pipeUpX by mutableStateOf(width.value)
    var pipeUpY by mutableStateOf(0f)
    var pipeUpHeight by mutableStateOf(pipeUpHeight)

   //val width by mutableStateOf(60.dp)

    var isCounted by mutableStateOf(false)
}