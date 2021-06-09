package com.github.nthily.flappybird

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.nthily.flappybird.ui.Game
import com.github.nthily.flappybird.ui.GameState

@Composable
fun Score(game: Game){
    if(game.gameState != GameState.Unstarted){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            contentAlignment = Alignment.TopCenter
        ){
            Text(
                text = game.score.toString(),
                style = MaterialTheme.typography.h3,
                fontWeight = FontWeight.W700,
                fontFamily = FontFamily(
                    Font(R.font.fb, FontWeight.W700)
                )
            )
        }
    }
}