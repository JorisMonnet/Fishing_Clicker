package hearc.dev_mobile.fishing_clicker.boat

import android.content.SharedPreferences
import hearc.dev_mobile.fishing_clicker.Money
import hearc.dev_mobile.fishing_clicker.R
import java.math.BigInteger


class Boat(
    val name: String,
    var efficiency: BigInteger,
    var resourceIdNumber: Int,
    var purchasePrice: Money,
    var indexList : Int
) {
    var drawableId = getDrawableId(resourceIdNumber)
    var upgradePrice: Money = Money(purchasePrice.value.multiply(BigInteger("2")))
    var level = 1L
    var isBought = false
    var resourceId = getResourceId(resourceIdNumber)

    fun createAttributes(){
        upgradePrice = Money(purchasePrice.value.multiply(BigInteger("2")))
        resourceId = getResourceId(resourceIdNumber)
        drawableId = getDrawableId(resourceIdNumber)
    }

    private fun getResourceId(resourceIdNumber: Int): Int {
        return when (resourceIdNumber) {
            1 -> R.id.boat1
            2 -> R.id.boat2
            3 -> R.id.boat3
            4 -> R.id.boat4
            5 -> R.id.boat5
            6 -> R.id.boat6
            7 -> R.id.boat7
            8 -> R.id.boat8
            9 -> R.id.boat9
            10 -> R.id.boat10
            else -> R.id.boat1
        }
    }

    private fun getDrawableId(resourceIdNumber: Int): Int {
        return when (resourceIdNumber) {
            1 -> R.drawable.ic_boat
            2 -> R.drawable.ic_boat2
            3 -> R.drawable.ic_boat3
            4 -> R.drawable.ic_boat4
            5 -> R.drawable.ic_fish_boat1
            6 -> R.drawable.ic_boat5
            7 -> R.drawable.ic_boat6
            8 -> R.drawable.ic_boat7
            9 -> R.drawable.ic_boat8
            10 -> R.drawable.ic_fish_boat1
            else -> R.drawable.ic_fish_boat1
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
            putString("Efficiency$indexList", efficiency.toString())
            putInt("ResourceIdNumber$indexList", resourceIdNumber)
            putString("PurchasePrice$indexList", purchasePrice.value.toString())
            putLong("LevelBoat$indexList", level)
            apply()
        }
    }
}
