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
import kotlin.math.pow

class BoatManager(private val mainActivity: MainActivity) {

    var boughtBoatList = LinkedList<Boat>()
    val boatList : LinkedList<Boat> = generateBoatList()
    private var boatIndex = 1
    private var baseEfficiency = BigInteger.ONE
    var currentNewBoatPrice: BigInteger = BigInteger.ONE//TODO TOSET GOOD VALUE

    private var navView: NavigationView = mainActivity.nav_view
    private var applicationContext: Context = mainActivity.applicationContext
    private var money = mainActivity.user.money

    private fun buyABoat() {
        boughtBoatList.add(
            Boat(
                baseEfficiency,
                boatIndex,
                currentNewBoatPrice.multiply(BigInteger("2")),
                mainActivity
            )
        )
        baseEfficiency = baseEfficiency.multiply(BigInteger.TEN)
        mainActivity.updateMoneyTextView(currentNewBoatPrice.negate())
        currentNewBoatPrice = currentNewBoatPrice.multiply(BigInteger("150"))
    }

    private fun generateBoatList() : LinkedList<Boat>{
        val list = LinkedList<Boat>()
        addToBoatList(list,R.id.boat1)
        addToBoatList(list,R.id.boat2)
        addToBoatList(list,R.id.boat3)
        return list
    }

    private fun addToBoatList(list : LinkedList<Boat>, id : Int) {
        list.add(
            Boat(
                BigInteger.valueOf(10.0.pow(list.size).toLong()),
                id,
                BigInteger.valueOf(2.0.pow(list.size).toLong()),
                mainActivity
            )
        )
    }

    fun createBoatMenuListener() {
        navView.setNavigationItemSelectedListener {
            for( i in 0 until boatList.size){
                if(boatList[i].resourceId==it.itemId){
                    boatTreatment(it,if(i+1==boatList.size) -1 else boatList[i+1].resourceId,i)
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
        if (money.value >= currentNewBoatPrice && boughtBoatList.size <= inxBoat) {
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
            money.value.subtract(currentNewBoatPrice)
            it.title =
                "Boat${inxBoat + 1} lvl ${boughtBoatList[inxBoat].level}- lvl up cost ${boughtBoatList[inxBoat].priceUpdate}$"
        } else if (boughtBoatList.size > inxBoat && (money.value.compareTo(boughtBoatList[inxBoat].priceUpdate) == 1 || money.value == boughtBoatList[inxBoat].priceUpdate)) {
            action = true
            boughtBoatList[inxBoat].increaseLevel()
            money.value.subtract(boughtBoatList[inxBoat].priceUpdate)
            Toast.makeText(
                applicationContext,
                "You upgraded boat ${inxBoat + 1} ",
                Toast.LENGTH_SHORT
            )
                .show()
        } else if (!(boughtBoatList.size > inxBoat && (money.value.compareTo(boughtBoatList[inxBoat].priceUpdate) == 1 || money.value == boughtBoatList[inxBoat].priceUpdate)) || !(money.value >= currentNewBoatPrice && boughtBoatList.size <= inxBoat)) {
            Toast.makeText(
                applicationContext,
                "Not enough money ! Go Fish !",
                Toast.LENGTH_SHORT
            )
                .show()
        }
        if (action) {
            val money = Money()
            money.value = boughtBoatList[inxBoat].priceUpdate
            it.title =
                "Boat${inxBoat + 1} lvl ${boughtBoatList[inxBoat].level}- lvl up cost ${money}$"
        }

    }
}
