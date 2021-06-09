package com.github.nthily.flappybird

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.github.nthily.flappybird.ui.Game
import com.github.nthily.flappybird.ui.GameState

@Composable

fun OverAlert(game: Game){
    if(game.gameState == GameState.Over){
        AlertDialog(
            onDismissRequest = {

            },
            title = {
                Text(
                    text = "Game Over",
                    fontWeight = FontWeight.W700,
                    style = MaterialTheme.typography.h6
                )
            },
            text = {
                Text(
                    text = "Your Score is ${game.score}",
                    style = MaterialTheme.typography.body1
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        game.restartGame()
                        game.score = 0
                    },
                ) {
                    Text(
                        "确认",
                        fontWeight = FontWeight.W700,
                        style = MaterialTheme.typography.button
                    )
                }
            },
        )
    }
}