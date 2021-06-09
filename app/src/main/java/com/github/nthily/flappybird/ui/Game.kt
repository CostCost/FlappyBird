package com.github.nthily.flappybird.ui

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
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
        // 鸟儿下落

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

                if(pipe.pipeDownHeight.value >= gameObject.limitHeight.value / 2 + bird.y.dp.value &&
                    (-pipe.pipeDownX.dp) >= gameObject.limitWidth / 2 - bird.width &&
                    (-pipe.pipeDownX.dp) <= gameObject.limitWidth / 2 + bird.width / 2
                ){
                    gameState = GameState.Over
                }
                if(pipe.pipeUpHeight.value >= gameObject.limitHeight.value / 2 - bird.y.dp.value &&
                    (-pipe.pipeUpX.dp) >= gameObject.limitWidth / 2 - bird.width &&
                    (-pipe.pipeUpX.dp) <= gameObject.limitWidth / 2 + bird.width / 2
                ){
                    gameState = GameState.Over
                }

                if(-pipe.pipeDownX.dp >= gameObject.limitWidth) {
                    remove()
                }

                if((-pipe.pipeDownX.dp) >= gameObject.limitWidth / 2 + bird.width / 2 && !pipe.isCounted){
                    score += 1
                    pipe.isCounted = true
                }

            }
        }
        if(gameState == GameState.Over){
            bird.y = gameObject.limitHeight.value / 2 - bird.height.value / 2
            pipe.forEach {
                it.pipeDownX = 0f
                it.pipeUpX = 0f
            }
        }

        if(bird.y.dp - bird.height / 2  >= gameObject.limitHeight / 2) {
            gameState = GameState.Over
        }

    }

    fun restartGame(){
        bird.y = 0f
        gameState = GameState.Unstarted
        pipe.clear()
        addPipe()
    }

    private fun remove(){
        while(pipe.iterator().hasNext()){
            pipe.remove()
        }
        addPipe()
    }

    private fun randomHeight(): Pair<Float, Float> {

        val totalHeight = gameObject.limitHeight.value.toInt() - (bird.height.value * 4).toInt()

        val value = Random.nextInt(0..totalHeight)
            .toFloat()

        return Pair(value, totalHeight - value)
    }


    fun addPipe(){
        val height = randomHeight()
        pipe.add(Pipe(height.first.dp, height.second.dp))
    }
}

