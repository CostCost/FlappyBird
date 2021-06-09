package com.github.nthily.flappybird

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.nthily.flappybird.ui.Bird
import com.github.nthily.flappybird.ui.BirdState
import com.github.nthily.flappybird.ui.Game
import com.github.nthily.flappybird.ui.GameState
import com.github.nthily.flappybird.ui.theme.FlappyBirdTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlappyBirdTheme {
                val game by remember{ mutableStateOf(Game()) }
                game.addPipe()
                GameUI(game)
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun GameUI(game:Game){


    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos {
                game.update(it)
            }
        }
    }


    val interactionSource = remember { MutableInteractionSource() }

    // 未开始游戏前的上下动画
    val infiniteTransition = rememberInfiniteTransition()
    val unStartedAnimation by infiniteTransition.animateValue(
        initialValue = 0.dp,
        targetValue = when(game.birdState){
            BirdState.Jumping -> 15.dp
            BirdState.Falling -> (-15).dp
        },
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 450),
            repeatMode = RepeatMode.Reverse
        )
    )

    if(game.gameState == GameState.Unstarted){
        if(unStartedAnimation == (-15).dp) game.birdState = BirdState.Jumping else game.birdState = BirdState.Falling
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

    Background()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                if(game.gameState != GameState.Over){
                    game.gameState = GameState.Running
                    game.birdState = BirdState.Jumping
                }
            }
            .offset(
                y = when(game.gameState){
                    GameState.Unstarted -> unStartedAnimation
                    else -> birdPosition.dp
                }
            )
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                game.gameObject.limitHeight = placeable.measuredHeight.toDp()
                game.gameObject.limitWidth = placeable.measuredWidth.toDp()
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
    OverAlert(game)

}
