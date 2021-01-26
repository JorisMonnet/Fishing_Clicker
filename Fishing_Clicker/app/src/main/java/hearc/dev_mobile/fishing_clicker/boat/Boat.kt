package hearc.dev_mobile.fishing_clicker.boat

import android.content.SharedPreferences
import hearc.dev_mobile.fishing_clicker.Money
import hearc.dev_mobile.fishing_clicker.R
import java.math.BigInteger


class Boat(
    val name: String, var efficiency: BigInteger, var resourceIdNumber: Int, var purchasePrice: Money
) {
    val drawableId: Int = R.drawable.ic_fish_boat1
    var upgradePrice: Money = Money(purchasePrice.value.multiply(BigInteger("2")))
    var level = 1L
    var isBought = false
    var resourceId = getResourceId(resourceIdNumber)

    private fun getResourceId(resourceIdNumber: Int): Int {
         return when(resourceIdNumber){
            1 -> R.id.boat1
            2 -> R.id.boat2
             else -> R.id.boat1
         }
    }

    /**
     * Function to get the title of the item in the navigation menu when upgrading them
     */
    fun getTitleUpgrade(): String {
        return "$name lvl $level- lvl up cost $upgradePrice$"
    }

    /**
     * Function to get the title of the item in the navigation menu when buying them
     */
    fun getTitlePurchase(): String {
        return "Buy $name for $purchasePrice $"
    }

    /**
     * Function to self update on call the level & then the efficiency of the boat
     */
    fun increaseLevel() {
        efficiency += BigInteger.valueOf(5 * ++level)
        upgradePrice.value += upgradePrice.value.divide(BigInteger("2"))
    }

    /**
     * function to save the data of the boat when quitting app
     * @param sharedPrefBoat the sharedPreferences
     */
    fun save(sharedPrefBoat: SharedPreferences) {
        with(sharedPrefBoat.edit()) {
            putString("Efficiency", efficiency.toString())
            putInt("ResourceId", resourceIdNumber)
            putString("UpgradePrice", upgradePrice.value.toString())
            putString("PurchasePrice",purchasePrice.value.toString())
            putLong("LevelBoat", level)
            apply()
        }
    }
}
