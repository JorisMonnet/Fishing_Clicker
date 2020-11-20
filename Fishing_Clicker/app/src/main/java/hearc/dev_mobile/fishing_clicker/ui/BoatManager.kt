package com.example.fishing_clicker.ui

import android.app.Application
import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.get
import com.example.fishing_clicker.R
import com.example.fishing_clicker.boat.Boat
import com.google.android.material.navigation.NavigationView
import java.util.*
import kotlin.math.pow

class BoatManager() {


    companion object {
        private var money = 50000
        private var boatList: MutableList<Boat> = LinkedList<Boat>()
        private var boatIndex = 1
        private var baseEfficiency = 1.0
        const val initPriceBoat = 100
        private var currentNewBoatPrice = 100

        fun buyABoat() {
            boatList.add(
                Boat(
                   // baseEfficiency, boatIndex, powInt(initPriceBoat, boatIndex++)TODO remove line under this
                    baseEfficiency, boatIndex, initPriceBoat)
            )
            baseEfficiency *= 1.5
            currentNewBoatPrice*=2
        }


        fun createBoatMenuListener(nav_view: NavigationView, applicationContext: Context) {
            nav_view.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.boat1 -> {

                        boatTreatment(it, nav_view, applicationContext, R.id.boat2, 0)

                    }
                    R.id.boat2 -> {
                        boatTreatment(it, nav_view, applicationContext, R.id.boat3, 1)
                    }
                    R.id.boat3 -> {
                        boatTreatment(it, nav_view, applicationContext, -1, 2)
                    }
                }
                true
            }
        }

        private fun boatTreatment(
            it: MenuItem,
            nav_view: NavigationView,
            applicationContext: Context, nextIdBoat: Int, inxBoat: Int
        ) {
            if (money >= currentNewBoatPrice && boatList.size <= inxBoat) {
                buyABoat()
                if (nextIdBoat != -1) {
                    nav_view.menu.findItem(nextIdBoat).isVisible = true
                    nav_view.menu.findItem(nextIdBoat).title="Buy Boat ${inxBoat+2} for $currentNewBoatPrice$"
                }
                Toast.makeText(
                    applicationContext,
                    "You bought boat ${inxBoat + 1} ",
                    Toast.LENGTH_SHORT
                )
                    .show()
                money -= initPriceBoat
                it.title = "Boat${inxBoat+1} lvl ${boatList[inxBoat].level}- lvl up cost ${boatList[inxBoat].priceUpdate}$"
            }
            else if ( boatList.size > inxBoat /*&& money >= boatList[inxBoat].priceUpdate*/ ) {
                boatList[inxBoat].increaseLevel()
                money -= boatList[inxBoat].priceUpdate
                Toast.makeText(
                    applicationContext,
                    "You upgraded boat ${inxBoat + 1} ",
                    Toast.LENGTH_SHORT
                )
                    .show()
                it.title = "Boat${inxBoat+1} lvl ${boatList[inxBoat].level}- lvl up cost ${boatList[inxBoat].priceUpdate}$"
            }

        }

        fun powInt(n: Int, exp: Int): Int {
            var temp = 1
            for (i in 1..exp) {
                temp *= n
            }
            return temp
        }
    }

}
