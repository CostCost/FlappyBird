package com.github.nthily.flappybird.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.github.nthily.flappybird.game.GameObject
import com.github.nthily.flappybird.game.Pipe
import java.util.*
import kotlin.random.Random
import kotlin.random.nextInt


enum class GameState{
    Unstarted, Running, Over
}

class Game {

    val gameObject = GameObject()
    val bird = Bird()
    val pipe by mutableStateOf<Queue<Pipe>>(LinkedList())


    var gameState by mutableStateOf(GameState.Unstarted)

    var birdState by mutableStateOf(BirdState.Falling)

    var score by mutableStateOf(0)

    fun update(time:Long){

        if(gameState == GameState.Running){
            when(birdState){
                BirdState.Jumping -> {
                    bird.jump(gameObject.jumpDistance)
                    birdState = BirdState.Falling
                }
                BirdState.Falling -> {
                    bird.y += 5f
                }
            }

            pipe.forEachIndexed { index, pipe ->
                pipe.pipeDownX -= 2f
                pipe.pipeUpX -= 2f

                // Up layer detection
                if(pipe.pipeDownHeight.value >= gameObject.screenHeight.value / 2 + bird.y.dp.value &&
                    (-pipe.pipeDownX.dp) + pipe.width >= gameObject.screenWidth / 2 - bird.width / 2 &&
                    (-pipe.pipeDownX.dp) <= gameObject.screenWidth / 2 + bird.width / 2
                ){
                    gameState = GameState.Over
                }

                // Down layer detection
                if(pipe.pipeUpHeight.value >= gameObject.screenHeight.value / 2 - bird.y.dp.value &&
                    (-pipe.pipeUpX.dp) + pipe.width >= gameObject.screenWidth / 2 - bird.width / 2 &&
                    (-pipe.pipeUpX.dp) <= gameObject.screenWidth / 2 + bird.width / 2
                ){
                    gameState = GameState.Over
                }

                // if the bird has reached the bottom
                if(bird.y.dp - bird.height / 2  >= gameObject.screenHeight / 2) {
                    gameState = GameState.Over
                }

                // if the bird has crossed a pipe
                if((-pipe.pipeDownX.dp) >= gameObject.screenWidth / 2 + bird.width && !pipe.isCounted){
                    pipe.isCounted = true
                    score += 1
                    gameObject.requestAdd = true
                }

            }
            if(gameObject.requestAdd){
                addPipe()
                gameObject.requestAdd = false
            }
        }
        if(gameState == GameState.Over){
            //bird.y = 0f
        }

    }

    fun restartGame(){
        gameState = GameState.Unstarted
        bird.y = 0f
        pipe.clear()
        score = 0
        addPipe()
    }
    private fun randomHeight(): Pair<Float, Float> {

        var totalHeight = 0

        if(gameObject.screenHeight != 0.dp) totalHeight = gameObject.screenHeight.value.toInt() - (bird.height.value * 4).toInt()

        val value = Random.nextInt(0..totalHeight)
            .toFloat()

        return Pair(value, totalHeight - value)
    }


    private fun addPipe(){
        val height = randomHeight()
        pipe.add(Pipe(height.first.dp, height.second.dp))
    }
}

