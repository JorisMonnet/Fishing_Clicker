package hearc.dev_mobile.fishing_clicker.ui

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.*
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

    private var boughtBoatList = LinkedList<Boat>()
    val boatList : LinkedList<Boat> = generateBoatList()

    private var boatIndex = 1
    private var baseEfficiency = BigInteger.ONE
    var currentNewBoatPrice: BigInteger = BigInteger.ONE//TODO TO SET GOOD VALUE

    private var navView: NavigationView = mainActivity.nav_view
    private var applicationContext: Context = mainActivity.applicationContext

    private var playerMoney = mainActivity.user.money

    private fun buyABoat() {
        boughtBoatList.add(
            Boat(
                baseEfficiency,
                boatIndex,
                Money(currentNewBoatPrice.multiply(BigInteger("2"))),
                mainActivity
            )
        )
        baseEfficiency = baseEfficiency.multiply(BigInteger.TEN)
        mainActivity.updateMoneyTextView(currentNewBoatPrice.negate())

        currentNewBoatPrice = currentNewBoatPrice.multiply(BigInteger("150"))

        val textView = TextView(mainActivity)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.text = "Boat n° ${boatIndex++}"

        val imageView =
            ImageView(mainActivity)
        imageView.layoutParams = LinearLayout.LayoutParams(400, 400)
        imageView.x = 2F // setting margin from left
        imageView.y = 2F // setting margin from top
        imageView.setImageResource(R.drawable.ic_fish_boat1)

        val layout = mainActivity.findViewById<LinearLayout>(R.id.imageLayout)
        layout?.addView(imageView)
        layout?.addView(textView)
    }

    private fun generateBoatList() : LinkedList<Boat>{
        val list = LinkedList<Boat>()
        addToBoatList(list,R.id.boat1)
        addToBoatList(list,R.id.boat2)
        addToBoatList(list,R.id.boat3)
        addToBoatList(list,R.id.boat4)
        addToBoatList(list,R.id.boat5)
        addToBoatList(list,R.id.boat6)
        addToBoatList(list,R.id.boat7)
        return list
    }

    private fun addToBoatList(list : LinkedList<Boat>, id : Int) {
        list.add(
            Boat(
                BigInteger.valueOf(10.0.pow(list.size).toLong()), //TODO find good value
                id,
                Money(BigInteger.valueOf(2.0.pow(list.size).toLong())), //TODO find good value
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

    private fun boatTreatment(it: MenuItem, nextIdBoat: Int, inxBoat: Int) {
        if (playerMoney.value >= currentNewBoatPrice && boughtBoatList.size <= inxBoat) {
            buyABoat()
            if (nextIdBoat != -1) {
                navView.menu.findItem(nextIdBoat).isVisible = true
                val tempMoney=Money(currentNewBoatPrice)
                navView.menu.findItem(nextIdBoat).title =
                    "Buy Boat ${inxBoat + 2} for $tempMoney $"
            }
            makeToast("You bought boat ${inxBoat + 1} ")
            playerMoney.value.subtract(currentNewBoatPrice)
            it.title = "Boat${inxBoat + 1} lvl ${boughtBoatList[inxBoat].level}- lvl up cost ${boughtBoatList[inxBoat].priceUpdate}$"
        } else if (boughtBoatList.size > inxBoat && (playerMoney.value.compareTo(boughtBoatList[inxBoat].priceUpdate.value) == 1 || playerMoney.value == boughtBoatList[inxBoat].priceUpdate.value)) {
            boughtBoatList[inxBoat].increaseLevel()
            playerMoney.value.subtract(boughtBoatList[inxBoat].priceUpdate.value)
            makeToast("You upgraded boat ${inxBoat + 1} ")
            it.title = "Boat${inxBoat + 1} lvl ${boughtBoatList[inxBoat].level}- lvl up cost ${boughtBoatList[inxBoat].priceUpdate}$"
        } else if (!(boughtBoatList.size > inxBoat && (playerMoney.value.compareTo(boughtBoatList[inxBoat].priceUpdate.value) == 1
                    || playerMoney.value == boughtBoatList[inxBoat].priceUpdate.value)) || !(playerMoney.value >= currentNewBoatPrice && boughtBoatList.size <= inxBoat)) {
            makeToast("Not enough money ! Go Fish !")
        }
    }

    private fun makeToast(message : String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}
