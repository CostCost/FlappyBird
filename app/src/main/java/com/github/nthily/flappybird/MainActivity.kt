package com.github.nthily.flappybird

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.flappybird.ui.BirdState
import com.github.nthily.flappybird.ui.Game
import com.github.nthily.flappybird.ui.GameState
import com.github.nthily.flappybird.ui.theme.FlappyBirdTheme

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlappyBirdTheme {
                val viewModel = hiltViewModel<UiState>()
                val game by remember{ mutableStateOf(Game()) }
                game.restartGame()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("FlappyBird")
                            },
                            actions = {
                                IconButton(onClick = { if(viewModel.type == 0) viewModel.type = 1  else viewModel.type = 0}) {
                                    Icon(painterResource(id = R.drawable.image), null)
                                }
                            },
                            backgroundColor = Color(0xFF0079D3)
                        )
                    }
                ){
                    GameUI(game)
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun GameUI(game:Game){


    val viewModel = hiltViewModel<UiState>()

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                game.update(it)
            }
        }
    }


    val interactionSource = remember { MutableInteractionSource() }

    // 未开始游戏前的上下动画

    val unStartedAnimation by animateFloatAsState(
        targetValue = when(game.birdState){
            BirdState.Jumping -> 25f
            BirdState.Falling -> (-25f)
        },
        tween(500)
    )


    if(game.gameState == GameState.Unstarted){
        if(unStartedAnimation == -25f) {
            game.birdState = BirdState.Jumping
        } else if(unStartedAnimation == 25f)game.birdState = BirdState.Falling
    }


    val birdPosition by animateFloatAsState(game.bird.y,
        tween(
            when(game.gameState){
                GameState.Running -> if(game.birdState == BirdState.Falling) 150 else 50
                GameState.Over -> 1300
                GameState.Unstarted -> 0
            }, easing = LinearEasing
        )
    )

    Crossfade(targetState = viewModel.type) {
        if(it == 0){
            Background(R.drawable.bkg)
        } else Background(R.drawable.bkg2)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                if (game.gameState != GameState.Over) {
                    game.gameState = GameState.Running
                    game.birdState = BirdState.Jumping
                }
            }
            .offset(
                y = when (game.gameState) {
                    GameState.Unstarted -> unStartedAnimation.dp
                    else -> birdPosition.dp
                }
            )
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                game.gameObject.screenHeight = placeable.measuredHeight.toDp()
                game.gameObject.screenWidth = placeable.measuredWidth.toDp()
                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            },
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(id = R.drawable.bird),
            contentDescription = null,
            modifier = Modifier
                .size(width = game.bird.width, height = game.bird.height)
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    game.bird.height = placeable.measuredHeight.toDp()
                    game.bird.width = placeable.measuredWidth.toDp()
                    layout(placeable.width, placeable.height) {
                        placeable.placeRelative(0, 0)
                    }
                },
            contentScale = ContentScale.FillBounds
        )
    }

    // 柱子
    game.pipe.forEach{ pipe ->

        val pipeDownX by animateFloatAsState(pipe.pipeDownX, tween(150, easing = LinearEasing))
        val pipeUpX by animateFloatAsState(pipe.pipeUpX, tween(150, easing = LinearEasing))

        if(game.gameState == GameState.Running || game.gameState == GameState.Over){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(x = pipeDownX.dp),
                contentAlignment = Alignment.TopEnd
            ){
                Image(
                    painter = painterResource(id = R.drawable.pipedown),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = pipe.width, height = pipe.pipeDownHeight),
                    contentScale = ContentScale.FillBounds
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(x = pipeUpX.dp),
                contentAlignment = Alignment.BottomEnd
            ){
                Image(
                    painter = painterResource(id = R.drawable.pipeup),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = pipe.width, height = pipe.pipeUpHeight),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }

    Score(game)
    OverAlert(game)

}
