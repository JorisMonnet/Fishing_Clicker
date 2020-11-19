package com.example.fishing_clicker.boat

import android.graphics.drawable.Drawable
import com.example.fishing_clicker.R
import kotlin.math.log10

class Boat(var efficiency: Double, private val index: Int) {

    //var init
    private var level=0;
    private var image = when (index) {
        1 -> R.drawable.ic_boat
        2 -> R.drawable.ic_boat2
        3 -> R.drawable.ic_boat3
     //   4 -> R.drawable.ic_boat4
     //   5 -> R.drawable.ic_boat5
        else -> R.drawable.ic_boat
    }

    /**
     * Function to self update on call the level & then the efficiency of the boat
     */
    fun increaseLevel() {
        level.inc()
        efficiency = efficiency*level/10
    }

    /**
     * Function to return the reward within a time period
     */
    fun doMoneyReward(elapsedSec: Double): Double {
        return elapsedSec * efficiency
    }
}