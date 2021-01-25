package hearc.dev_mobile.fishing_clicker.boat

import android.content.SharedPreferences
import hearc.dev_mobile.fishing_clicker.MainActivity
import hearc.dev_mobile.fishing_clicker.Money
import java.math.BigInteger


class Boat(
    val name : String, var efficiency: BigInteger, var resourceId: Int, val purchasePrice : Money,
    private val mainActivity: MainActivity) {
    var upgradePrice: Money = Money(purchasePrice.value.multiply(BigInteger("2")))
    var level = 1L
    var isBought = false

    /**
     * Function to self update on call the level & then the efficiency of the boat
     */
    fun increaseLevel() {
        level++
        efficiency = efficiency.add(BigInteger.valueOf(5*level))
        mainActivity.updateMoneyTextView(upgradePrice.value.negate())
        upgradePrice.value+=upgradePrice.value.divide(BigInteger("2"))
    }

    /**
     * Function to return the reward within a time period
     * @param eSec the time period
     */
    fun doMoneyReward(eSec: Long) {
        mainActivity.updateMoneyTextView(BigInteger.valueOf((efficiency.multiply(BigInteger.valueOf(eSec))).toLong()))
    }

    /**
     * function to save the data of the boat when quitting app
     * @param sharedPrefBoat the sharedPreferences
     */
    fun save(sharedPrefBoat : SharedPreferences){
        with(sharedPrefBoat.edit()){
            putString("Efficiency",efficiency.toString())
            putInt("ResourceId",resourceId)
            putString("UpgradePrice",upgradePrice.value.toString())
            putLong("LevelBoat",level)
            apply()
        }
    }
}
