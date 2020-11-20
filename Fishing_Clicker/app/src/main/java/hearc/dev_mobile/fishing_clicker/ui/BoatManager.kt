package hearc.dev_mobile.fishing_clicker.ui

import android.content.Context
import android.view.MenuItem
import android.widget.Toast
import hearc.dev_mobile.fishing_clicker.boat.Boat
import com.google.android.material.navigation.NavigationView
import hearc.dev_mobile.fishing_clicker.MainActivity
import hearc.dev_mobile.fishing_clicker.Money
import hearc.dev_mobile.fishing_clicker.R
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigInteger
import java.util.*

class BoatManager(private val mainActivity: MainActivity) {


    var boatList = LinkedList<Boat>()
    private var boatIndex = 1
    private var baseEfficiency = BigInteger.valueOf(1)
    val initPriceBoat: BigInteger = BigInteger.valueOf(100)
    private var currentNewBoatPrice = BigInteger.valueOf(100)

    private var navView: NavigationView = mainActivity.nav_view
    private var applicationContext: Context = mainActivity.applicationContext
    private var money = mainActivity.generalMoney


    private fun buyABoat() {
        boatList.add(
            Boat(
                baseEfficiency,
                boatIndex,
                initPriceBoat.multiply(BigInteger.valueOf(2)),
                mainActivity
            )
        )
        baseEfficiency = baseEfficiency.multiply(BigInteger.valueOf(10))
        mainActivity.updateMoneyTextView(currentNewBoatPrice.negate())
        currentNewBoatPrice = currentNewBoatPrice.multiply(BigInteger.valueOf(150))
    }


    fun createBoatMenuListener() {
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.boat1 -> {

                    boatTreatment(it, R.id.boat2, 0)

                }
                R.id.boat2 -> {
                    boatTreatment(it, R.id.boat3, 1)
                }
                R.id.boat3 -> {
                    boatTreatment(it, -1, 2)
                }
            }
            true
        }
    }

    private fun boatTreatment(
        it: MenuItem,
        nextIdBoat: Int, inxBoat: Int
    ) {
        var action = false
        if (money.value >= currentNewBoatPrice && boatList.size <= inxBoat) {
            action = true
            buyABoat()
            if (nextIdBoat != -1) {
                navView.menu.findItem(nextIdBoat).isVisible = true
                navView.menu.findItem(nextIdBoat).title =
                    "Buy Boat ${inxBoat + 2} for $currentNewBoatPrice$"
            }
            Toast.makeText(
                applicationContext,
                "You bought boat ${inxBoat + 1} ",
                Toast.LENGTH_SHORT
            )
                .show()
            money.value.subtract(initPriceBoat)
            it.title =
                "Boat${inxBoat + 1} lvl ${boatList[inxBoat].level}- lvl up cost ${boatList[inxBoat].priceUpdate}$"
        } else if (boatList.size > inxBoat && (money.value.compareTo(boatList[inxBoat].priceUpdate) == 1 || money.value == boatList[inxBoat].priceUpdate)) {
            action = true
            boatList[inxBoat].increaseLevel()
            money.value.subtract(boatList[inxBoat].priceUpdate)
            Toast.makeText(
                applicationContext,
                "You upgraded boat ${inxBoat + 1} ",
                Toast.LENGTH_SHORT
            )
                .show()
        } else if (!(boatList.size > inxBoat && (money.value.compareTo(boatList[inxBoat].priceUpdate) == 1 || money.value == boatList[inxBoat].priceUpdate)) || !(money.value >= currentNewBoatPrice && boatList.size <= inxBoat)) {
            Toast.makeText(
                applicationContext,
                "Not enough money ! Go Fish !",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        if (action) {
            val money = Money()
            money.value = boatList[inxBoat].priceUpdate
            it.title =
                "Boat${inxBoat + 1} lvl ${boatList[inxBoat].level}- lvl up cost ${money}$"
        }

    }
}