package hearc.dev_mobile.fishing_clicker.boat

import hearc.dev_mobile.fishing_clicker.MainActivity
import hearc.dev_mobile.fishing_clicker.Money
import hearc.dev_mobile.fishing_clicker.R
import java.math.BigInteger


class Boat(
    val name : String, private var efficiency: BigInteger, val resourceId: Int, val purchasePrice : Money,
    private val mainActivity: MainActivity) {
    var priceUpdate: Money = Money(purchasePrice.value.multiply(BigInteger("2")))
    var level = 1L
    var bought = false
    private var imageId = when (resourceId) {
        R.id.boat1 -> R.drawable.ic_fish_boat1
        1 -> R.drawable.ic_boat2
        2 -> R.drawable.ic_boat3
        else -> R.drawable.ic_boat
    }

    /**
     * Function to self update on call the level & then the efficiency of the boat
     */
    fun increaseLevel() {
        level++
        efficiency = efficiency.add(BigInteger.valueOf(5*level))
        mainActivity.updateMoneyTextView(priceUpdate.value.negate())
        priceUpdate.value+=priceUpdate.value.divide(BigInteger("2"))
    }

    /**
     * Function to return the reward within a time period
     */
    fun doMoneyReward(eSec: Long) {
        mainActivity.updateMoneyTextView(BigInteger.valueOf((efficiency.multiply(BigInteger.valueOf(eSec))).toLong()))
    }

}
