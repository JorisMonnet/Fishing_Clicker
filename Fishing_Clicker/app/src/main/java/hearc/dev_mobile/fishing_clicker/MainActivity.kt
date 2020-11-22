package hearc.dev_mobile.fishing_clicker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentContainerView
import hearc.dev_mobile.fishing_clicker.ui.BoatManager
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.math.BigInteger
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var boatManager: BoatManager
    var user : User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user.createValuesFromPref(getSharedPreferences("Preferences",Context.MODE_PRIVATE))
        setContentView(R.layout.activity_main)
        boatManager = BoatManager(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        val contentLayout : FragmentContainerView = findViewById(R.id.FragmentContainerView)
        updateMoneyTextView(BigInteger.ZERO)
        contentLayout.setOnClickListener {
            updateMoneyTextView(user.getClickValue())
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        boatManager.createBoatMenuListener()
        Thread {//AFK MECHANISM
            while (true) {
                for (boat in boatManager.boatList) {
                    // try to touch View of UI thread
                    this@MainActivity.runOnUiThread {
                        if(boat.bought)
                            boat.doMoneyReward(1L)
                    }
                }
                try {
                    Thread.sleep(999)
                } catch (e: Exception) {
                    Log.d("ThreadSleepError", e.toString())
                }
            }
        }.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        nav_view.menu.findItem(R.id.boat1).title = "${boatManager.boatList[0].name} cost ${boatManager.boatList[0].purchasePrice}$"
        return true
    }

    fun updateMoneyTextView(valueToAdd : BigInteger){
        val text: TextView = findViewById(R.id.moneyTextView)
        user.money.value = user.money.value.add(valueToAdd)
        text.text = user.money.toString()
        if(user.money.value.compareTo(BigInteger.valueOf(10.0.pow(user.level*3+6).toLong()))==1){
            user.level++
            //TODO()  //change background when changing of level
            //contentLayout.background = R.drawable.bgLevel1
        }
    }

    override fun onPause() {
        user.saveData(getSharedPreferences("Preferences",Context.MODE_PRIVATE))
        super.onPause()
    }

    override fun onResume() {
        user.createValuesFromPref(getSharedPreferences("Preferences",Context.MODE_PRIVATE))
        super.onResume()
    }

    override fun onRestart() {
        user.saveData(getSharedPreferences("Preferences",Context.MODE_PRIVATE))
        super.onRestart()
    }

    override fun onDestroy() {
        user.saveData(getSharedPreferences("Preferences",Context.MODE_PRIVATE))
        super.onDestroy()
    }
}
