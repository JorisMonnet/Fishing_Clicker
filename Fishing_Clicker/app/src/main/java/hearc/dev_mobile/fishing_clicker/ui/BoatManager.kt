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

    val boatList : LinkedList<Boat> = generateBoatList()

    private var navView: NavigationView = mainActivity.nav_view
    private var applicationContext: Context = mainActivity.applicationContext
    private var displayedBoat=0
    private var playerMoney = mainActivity.user.money

    private fun buyBoat(index : Int) {
        displayedBoat++
        mainActivity.updateMoneyTextView(boatList[index].purchasePrice.value.negate())

        val textView = TextView(mainActivity)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        textView.text = boatList[index].name

        val imageView =
            ImageView(mainActivity)
        imageView.layoutParams = LinearLayout.LayoutParams(400, 400)
        imageView.x = 2F // setting margin from left
        imageView.y = 2F // setting margin from top
        imageView.setImageResource(R.drawable.ic_fish_boat1)

        val layout = mainActivity.findViewById<LinearLayout>(R.id.imageLayout)
        layout.addView(imageView)
        layout.addView(textView)
    }

    private fun generateBoatList() : LinkedList<Boat>{
        val list = LinkedList<Boat>()
        addToBoatList(list,R.id.boat1,0)
        addToBoatList(list,R.id.boat2,1)
        addToBoatList(list,R.id.boat3,2)
        addToBoatList(list,R.id.boat4,3)
        addToBoatList(list,R.id.boat5,4)
        addToBoatList(list,R.id.boat6,5)
        addToBoatList(list,R.id.boat7,6)
        return list
    }

    private fun addToBoatList(list : LinkedList<Boat>, id : Int,indexList : Int) {
        list.add(
            Boat(
                "Boat ${indexList+1}",
                BigInteger.valueOf(10.0.pow(list.size).toLong()),
                id,
                Money(BigInteger.valueOf(150.0.pow(indexList).toLong())),
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
        if (playerMoney.value >= boatList[inxBoat+1].priceUpdate.value && displayedBoat <= inxBoat) {
            buyBoat(inxBoat)
            if (nextIdBoat != -1) {
                navView.menu.findItem(nextIdBoat).isVisible = true
                navView.menu.findItem(nextIdBoat).title ="Buy ${boatList[inxBoat+1].name} for ${boatList[inxBoat+1].purchasePrice} $"
            }
            makeToast("You bought ${boatList[inxBoat].name} ")
            playerMoney.value.subtract(boatList[inxBoat].priceUpdate.value)
            it.title = "${boatList[inxBoat].name} lvl ${boatList[inxBoat].level}- lvl up cost ${boatList[inxBoat].priceUpdate}$"
        } else if (boatList.size > inxBoat && (playerMoney.value.compareTo(boatList[inxBoat].priceUpdate.value) == 1 || playerMoney.value == boatList[inxBoat].priceUpdate.value)) {
            boatList[inxBoat].increaseLevel()
            playerMoney.value.subtract(boatList[inxBoat].priceUpdate.value)
            makeToast("You upgraded ${boatList[inxBoat].name} ")
            it.title = "${boatList[inxBoat].name} lvl ${boatList[inxBoat].level}- lvl up cost ${boatList[inxBoat].priceUpdate}$"
        } else if (!(boatList.size > inxBoat && (playerMoney.value.compareTo(boatList[inxBoat].priceUpdate.value) == 1
                    || playerMoney.value == boatList[inxBoat].priceUpdate.value)) || !(playerMoney.value >= boatList[inxBoat+1].priceUpdate.value && boatList.size <= inxBoat)) {
            makeToast("Not enough money ! Go Fish !")
        }
    }

    private fun makeToast(message : String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }

    private fun saveData(){

    }
}
