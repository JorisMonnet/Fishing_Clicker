package com.example.fishing_clicker.boat

import android.graphics.drawable.Drawable
import com.example.fishing_clicker.R
import kotlin.math.log10

class Boat(var efficiency: Double, private val index: Int, var priceUpdate: Int) {



    var level = 1;
    private var imageId = when (index) {
        0 -> R.drawable.ic_boat
        1 -> R.drawable.ic_boat2
        2 -> R.drawable.ic_boat3
        else -> R.drawable.ic_boat
    }

    fun getImageId(): Int {
        return imageId
    }

    /**
     * Function to self update on call the level & then the efficiency of the boat
     */
    fun increaseLevel() {
        level+=1
        efficiency = efficiency * level / 10
        priceUpdate*=2
    }

    /**
     * Function to return the reward within a time period
     */
    fun doMoneyReward(elapsedSec: Double): Double {
        return elapsedSec * efficiency
    }
}
