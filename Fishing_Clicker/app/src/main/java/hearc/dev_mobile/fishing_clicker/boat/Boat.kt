package hearc.dev_mobile.fishing_clicker.boat

import hearc.dev_mobile.fishing_clicker.MainActivity
import hearc.dev_mobile.fishing_clicker.R
import java.math.BigInteger


class Boat(var efficiency: BigInteger, private val index: Int, var priceUpdate: BigInteger,val mainActivity: MainActivity) {



    var level = 1L

    private var imageId = when (index) {
        0 -> R.drawable.ic_fish_boat1
        1 -> R.drawable.ic_boat2
        2 -> R.drawable.ic_boat3
        else -> R.drawable.ic_boat
    }

    /**
     * Function to self update on call the level & then the efficiency of the boat
     */
    fun increaseLevel() {
        level+=1
        efficiency = efficiency.add(BigInteger.valueOf(5*level))
        mainActivity.updateMoneyTextView(priceUpdate.negate())
        priceUpdate+=priceUpdate.divide(BigInteger.valueOf(2))
    }

    /**
     * Function to return the reward within a time period
     */
    fun doMoneyReward(eSec: Long) {
        mainActivity.updateMoneyTextView(BigInteger.valueOf((efficiency.multiply(BigInteger.valueOf(eSec))).toLong()))
    }
}
