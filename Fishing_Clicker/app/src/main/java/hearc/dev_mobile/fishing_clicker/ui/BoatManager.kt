package hearc.dev_mobile.fishing_clicker.ui

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.*
import hearc.dev_mobile.fishing_clicker.model.boat.Boat
import com.google.android.material.navigation.NavigationView
import hearc.dev_mobile.fishing_clicker.MainActivity
import hearc.dev_mobile.fishing_clicker.model.money.Money
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
    private var toast : Toast = Toast.makeText(applicationContext,"",Toast.LENGTH_SHORT)

    /**
     * function to create boat/buy boat and show them into the main view
     * @param index the index of the boat to buy in the boat list
     * @param buying if the boat is created from the sharedPreferences or bought
     */
    private fun createBoat(index : Int, buying : Boolean) {
        displayedBoat++
        boatList[index].isBought=true
        if(buying)
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

    /**
     * Generate the boat list
     * @return this list
     */
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

    /**
     * add a boat to the list
     * @param list the list where the boat is added
     * @param id the resourceId of the boat
     * @param indexList the index list of the boat to know where
     * it will be in the list ad change parameter of the boat with it
     */
    private fun addToBoatList(list : LinkedList<Boat>, id : Int,indexList : Int) {
        list.add(
            Boat(
                "Boat ${indexList+1}",
                BigInteger.valueOf(10.0.pow(indexList).toLong()),
                id,
                Money(BigInteger.valueOf(150.0.pow(indexList).toLong())),
                mainActivity
            )
        )
    }

    /**
     * Listen to the click into the navigationView on an item(the different boat)
     */
    fun createBoatMenuListener() {
        navView.setNavigationItemSelectedListener {
            for( i in 0 until boatList.size){
                if(boatList[i].resourceId==it.itemId){
                    boatTreatment(it,i)
                }
            }
            true
        }
    }

    /**
     * Function which use the item clicked and decide what to do, buy it, upgrade it
     * or saying that there is not enough money to do one of these
     * @param indexBoat the index of the boat into the boat List
     * @param it the menuItem
     */
    private fun boatTreatment(it: MenuItem, indexBoat: Int) {
        if (playerMoney.value >= boatList[indexBoat].purchasePrice.value && displayedBoat <= indexBoat) {
            createBoat(indexBoat,true)
            if (indexBoat != boatList.size-1) {
                navView.menu.findItem(boatList[indexBoat+1].resourceId).isVisible = true
                navView.menu.findItem(boatList[indexBoat+1].resourceId).title ="Buy ${boatList[indexBoat+1].name} for ${boatList[indexBoat+1].purchasePrice} $"
            }
            makeToast("You bought ${boatList[indexBoat].name}")
            playerMoney.value.subtract(boatList[indexBoat].purchasePrice.value)
            it.title = "${boatList[indexBoat].name} lvl ${boatList[indexBoat].level}- lvl up cost ${boatList[indexBoat].upgradePrice}$"
        } else if (displayedBoat > indexBoat && (playerMoney.value.compareTo(boatList[indexBoat].upgradePrice.value) == 1 || playerMoney.value == boatList[indexBoat].upgradePrice.value)) {
            boatList[indexBoat].increaseLevel()
            playerMoney.value.subtract(boatList[indexBoat].upgradePrice.value)
            makeToast("You upgraded ${boatList[indexBoat].name} ")
            it.title = "${boatList[indexBoat].name} lvl ${boatList[indexBoat].level}- lvl up cost ${boatList[indexBoat].upgradePrice}$"
        } else {
            makeToast("Not enough money ! Go Fish !")
        }
    }

    /**
     * Create a Toast message on what the user just did, delete the precedent if existing
     * @param message the message to display
     */
    private fun makeToast(message : String){
        toast.cancel()
        toast = Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT)
        toast.show()
    }
/*
    /**
     * save the data of all the boats bought
     * @param sharedPrefBoat the shared preferences
     * WORK IN PROGRESS TODO
     */
    fun saveData(sharedPrefBoat : SharedPreferences){
        for(boat in boatList){
            if(boat.isBought){
                boat.save(sharedPrefBoat)
            }
        }
        sharedPrefBoat.edit().putInt("DisplayedBoat",displayedBoat).apply()
    }
    /**
     * Get the data of the boat bought
     * @param sharedPrefBoat the shared preferences
     * WORK IN PROGRESS TODO
     */
    fun createBoatData(sharedPrefBoat : SharedPreferences){
        displayedBoat = sharedPrefBoat.getInt("DisplayedBoat",0)
        if(displayedBoat!=0) {
            for (i in 0 until displayedBoat) {
                createBoat(i,false)
                boatList[i].level = sharedPrefBoat.getLong("Level", 1L)
                boatList[i].upgradePrice =
                    Money(BigInteger(sharedPrefBoat.getString("UpgradePrice", "0")))
                boatList[i].resourceId = sharedPrefBoat.getInt("ResourceId", 0)
                boatList[i].efficiency = BigInteger(sharedPrefBoat.getString("Efficiency", ""))
                navView.menu.findItem(boatList[i].resourceId).isVisible = true
                navView.menu.findItem(boatList[i].resourceId).title = "${boatList[i].name} lvl ${boatList[i].level}- lvl up cost ${boatList[i].upgradePrice}$"
            }
        }
    }*/
}
