package hearc.dev_mobile.fishing_clicker.boat

import hearc.dev_mobile.fishing_clicker.MainActivity
import hearc.dev_mobile.fishing_clicker.Money
import hearc.dev_mobile.fishing_clicker.R
import java.math.BigInteger


class Boat(var efficiency: Double, private val index: Int, var priceUpdate: BigInteger,val mainActivity: MainActivity) {



    var level = 1

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
        efficiency += level*5
        mainActivity.updateMoneyTextView(priceUpdate.negate())
        priceUpdate+=priceUpdate.divide(BigInteger.valueOf(2))
    }

    /**
     * Function to return the reward within a time period
     */
    fun doMoneyReward(eSec: Double) {
        mainActivity.updateMoneyTextView(BigInteger.valueOf((efficiency*eSec).toLong()))
    }

    fun powInt(n: Long, exp: Long): Long {
        var temp = 1L
        for (i in 1..exp) {
            temp *= n
        }
        return temp
    }
}
