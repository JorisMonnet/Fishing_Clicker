package hearc.dev_mobile.fishing_clicker

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hearc.dev_mobile.fishing_clicker.ui.BoatManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pop_up_shake.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.math.BigInteger
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.pow

open class MainActivity : AppCompatActivity() {

    private var isDisplayingShake = true
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var boatManager: BoatManager
    var user: User = User()
    var percentToAddAfterShakeEvent = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user.createValuesFromPref(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        setContentView(R.layout.activity_main)
        boatManager = BoatManager(this)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            //open drawer when clicking on the button
            drawerLayout.openDrawer(GravityCompat.START)
        }
        val contentLayout: FragmentContainerView = findViewById(R.id.FragmentContainerView)
        updateMoneyTextView(BigInteger.ZERO)
        contentLayout.setOnClickListener {
            updateMoneyTextView(user.getClickValue())
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        boatManager.createBoatMenuListener()
        nav_view.menu.findItem(R.id.boat1).title =
            "${boatManager.boatList[0].name} cost ${boatManager.boatList[0].purchasePrice}$"
        Thread {//AFK MECHANISM
            while (true) {
                for (boat in boatManager.boatList) {
                    // try to touch View of UI thread
                    this@MainActivity.runOnUiThread {
                        if (boat.isBought)
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

        Thread {
            while (true) {
                if (!boatManager.boatList.isEmpty()) {
                    if (!isDisplayingShake) {
                        isDisplayingShake = true
                        val intent = Intent(this.applicationContext, PopUpShake::class.java)
                        startActivity(intent)
                    } else {
                        try {
                            Thread.sleep(ThreadLocalRandom.current().nextInt(40000, 90000).toLong())
                            isDisplayingShake = false
                        } catch (e: Exception) {
                            Log.d("ThreadSleepError", e.toString())
                        }
                    }
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
        return true
    }

    fun doShakeReward() {
        updateMoneyTextView(
            BigInteger.valueOf(
                BigInteger.valueOf(percentToAddAfterShakeEvent.toLong()).divide(user.money.value)
                    .toLong()
            )
        )
        setContentView(R.layout.activity_main)
        percentToAddAfterShakeEvent = 1
    }

    fun updateMoneyTextView(valueToAdd: BigInteger) {
        user.money.value = user.money.value.add(valueToAdd)
        moneyTextView.text = user.money.toString()

        if (user.money.value.compareTo(
                BigInteger.valueOf(
                    10.0.pow(user.level * 3 + 6).toLong()
                )
            ) == 1
        ) {
            user.level++
            Log.v("level", user.level.toString())
            //TODO()  //change background when changing of level
            main.setBackgroundColor(
                resources.getColor(
                    when (user.level % 17) {
                        0 -> R.color.colorMainBG0
                        1 -> R.color.colorMainBG1
                        2 -> R.color.colorMainBG2
                        3 -> R.color.colorMainBG3
                        4 -> R.color.colorMainBG4
                        5 -> R.color.colorMainBG5
                        6 -> R.color.colorMainBG6
                        7 -> R.color.colorMainBG7
                        8 -> R.color.colorMainBG8
                        9 -> R.color.colorMainBG9
                        10 -> R.color.colorMainBG10
                        11 -> R.color.colorMainBG11
                        12 -> R.color.colorMainBG12
                        13 -> R.color.colorMainBG13
                        14 -> R.color.colorMainBG14
                        15 -> R.color.colorMainBG15
                        16 -> R.color.colorMainBG16
                        else -> R.color.colorMainBG0
                    } as Int
                )
            )
        }
    }

    override fun onPause() {
        user.saveData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        super.onPause()
    }

    override fun onResume() {
        user.createValuesFromPref(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        super.onResume()
    }

    override fun onRestart() {
        user.saveData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        super.onRestart()
    }

    override fun onDestroy() {
        user.saveData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        super.onDestroy()
    }
}
