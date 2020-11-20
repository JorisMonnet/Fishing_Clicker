package hearc.dev_mobile.fishing_clicker.ui

import android.content.Context
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import hearc.dev_mobile.fishing_clicker.boat.Boat
import com.google.android.material.navigation.NavigationView
import hearc.dev_mobile.fishing_clicker.MainActivity
import hearc.dev_mobile.fishing_clicker.Money
import hearc.dev_mobile.fishing_clicker.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.math.BigInteger
import java.util.*

class BoatManager(val mainActivity: MainActivity) {


    var boatList = LinkedList<Boat>()
    private var boatIndex = 1
    private var baseEfficiency = 1.0
    val initPriceBoat: BigInteger = BigInteger.valueOf(100)
    private var currentNewBoatPrice = BigInteger.valueOf(100)

    private var nav_view: NavigationView = mainActivity.nav_view
    private var applicationContext: Context = mainActivity.applicationContext
    var money = mainActivity.generalMoney


    private fun buyABoat() {
        boatList.add(
            Boat(
                // baseEfficiency, boatIndex, powInt(initPriceBoat, boatIndex++,mainActivity)TODO remove line under this (it's here only for test)
                baseEfficiency, boatIndex, initPriceBoat, mainActivity
            )
        )
        baseEfficiency *= 1.5
        mainActivity.updateMoneyTextView(currentNewBoatPrice.negate())
        currentNewBoatPrice = currentNewBoatPrice.multiply(BigInteger.valueOf(2))
    }


    fun createBoatMenuListener() {
        nav_view.setNavigationItemSelectedListener {
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
        if (money.value >= currentNewBoatPrice && boatList.size <= inxBoat) {
            buyABoat()
            if (nextIdBoat != -1) {
                nav_view.menu.findItem(nextIdBoat).isVisible = true
                nav_view.menu.findItem(nextIdBoat).title =
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
            boatList[inxBoat].increaseLevel()
            money.value.subtract(boatList[inxBoat].priceUpdate)
            Toast.makeText(
                applicationContext,
                "You upgraded boat ${inxBoat + 1} ",
                Toast.LENGTH_SHORT
            )
                .show()
            it.title =
                "Boat${inxBoat + 1} lvl ${boatList[inxBoat].level}- lvl up cost ${boatList[inxBoat].priceUpdate}$"
        }

    }
}
