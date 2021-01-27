package hearc.dev_mobile.fishing_clicker.model.boat

import android.content.SharedPreferences
import hearc.dev_mobile.fishing_clicker.R
import hearc.dev_mobile.fishing_clicker.model.money.Money
import java.math.BigInteger

/**
 * Class managing Boats which give idle money
 */
class Boat(
    val name: String,
    var efficiency: BigInteger,
    private var resourceIdNumber: Int,
    var purchasePrice: Money,
    private var indexList: Int
) {
    var drawableId = getDrawableId(resourceIdNumber)
    var upgradePrice: Money = Money(purchasePrice.value.multiply(BigInteger("2")))
    var level = 1L
    var isBought = false
    var resourceId = getResourceId(resourceIdNumber)

    /**
     * Create the upgradePrice, resourceId and drawableId
     */
    fun createAttributes() {
        upgradePrice = Money(purchasePrice.value.multiply(BigInteger("2")))
        resourceId = getResourceId(resourceIdNumber)
        drawableId = getDrawableId(resourceIdNumber)
    }

    /**
     * return the resource Id associated to the resourceIdNumber
     * @param resourceIdNumber Int
     * @return resourceId
     */
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

    /**
     * Return the drawable for the given resourceId
     * @param resourceIdNumber Int
     * @return the drawable
     */
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
            10 -> R.drawable.ic_fish_boat2
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
