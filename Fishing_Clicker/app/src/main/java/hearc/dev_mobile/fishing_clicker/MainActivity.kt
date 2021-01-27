package hearc.dev_mobile.fishing_clicker

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentContainerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hearc.dev_mobile.fishing_clicker.model.user.User
import hearc.dev_mobile.fishing_clicker.ui.BoatManager
import hearc.dev_mobile.fishing_clicker.ui.activities.PopUpAbout
import hearc.dev_mobile.fishing_clicker.ui.activities.PopUpShake
import hearc.dev_mobile.fishing_clicker.ui.activities.PopUpSpecs
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pop_up_shake.*
import kotlinx.android.synthetic.main.activity_pop_up_specs.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.math.BigInteger
import java.util.concurrent.ThreadLocalRandom


open class MainActivity : AppCompatActivity() {

    private var isDisplayingShake = true
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var boatManager: BoatManager
    var user: User = User()
    var percentToAddAfterShakeEvent = 1

    companion object {
        var mediaPlayer: MediaPlayer? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get the user Preferences
        user.createValuesFromPref(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        setContentView(R.layout.activity_main)
        boatManager = BoatManager(this)
        boatManager.createBoatData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        boatManager.createBoatMenuListener()
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

        about_button.setOnClickListener {
            val intent = Intent(this, PopUpAbout::class.java)
            startActivity(intent)
        }
        specs_button.setOnClickListener {
            val intent = Intent(this, PopUpSpecs::class.java)
            intent.putExtra("eff", boatManager.globalEfficiency.toString())
            startActivity(intent)
        }

        boatManager.createBoatMenuListener()
        nav_view.menu.findItem(R.id.boat1).title =
            "${boatManager.boatList[0].name} cost ${boatManager.boatList[0].purchasePrice}$"

        goldenFish.setOnClickListener {
            Log.d("TAG", "onCreate: click")
            goldenFish.visibility = View.GONE
            val intent = Intent(this.applicationContext, PopUpShake::class.java)
            startActivity(intent)
        }

        blueFish.setOnClickListener {
            Log.d("TAG", "onCreate: click")
            user.money.value *= BigInteger.TEN
            updateMoneyTextView(BigInteger.ZERO)
            blueFish.visibility = View.GONE
        }

        shark.setOnClickListener {
            Log.d("TAG", "onCreate: click")
            user.money.value /= BigInteger.TEN
            updateMoneyTextView(BigInteger.ZERO)
            shark.visibility = View.GONE
        }

        Thread {//AFK MECHANISM
            while (true) {
                var cumulativeEfficiency = BigInteger.ZERO
                for (boat in boatManager.boatList) {
                    // try to touch View of UI thread
                    if (boat.isBought) {
                        this@MainActivity.runOnUiThread {
                            updateMoneyTextView(boat.efficiency)
                        }
                        cumulativeEfficiency = cumulativeEfficiency.add(boat.efficiency)
                    }
                }
                try {
                    Thread.sleep(999)
                } catch (e: Exception) {
                    Log.d("ThreadSleepError", e.toString())
                }

                if (cumulativeEfficiency > boatManager.globalEfficiency.value) {
                    boatManager.globalEfficiency.value = cumulativeEfficiency
                }
            }
        }.start()

        Thread {
            while (true) {
                if (!boatManager.boatList.isEmpty()) {
                    if (!isDisplayingShake && ThreadLocalRandom.current()
                            .nextInt(40000, 90000) % 75 == 0
                    ) {
                        isDisplayingShake = true
                        this@MainActivity.runOnUiThread {
                            goldenFish.visibility = View.VISIBLE
                            goldenFish.bringToFront()
                        }
                    } else {
                        isDisplayingShake = false
                    }
                }
                //1 time per 50
                if (ThreadLocalRandom.current().nextInt(0, 1000) % 50 == 0) {
                    try {
                        this@MainActivity.runOnUiThread {
                            blueFish.visibility = View.VISIBLE
                            blueFish.bringToFront()
                        }
                        Thread.sleep(ThreadLocalRandom.current().nextInt(2500, 5000).toLong())
                    } catch (e: Exception) {
                        Log.d("ThreadSleepError", e.toString())
                    }
                }
                //1 time per 20
                else if (ThreadLocalRandom.current().nextInt(0, 10000) % 20 == 0) {
                    try {
                        this@MainActivity.runOnUiThread {
                            shark.visibility = View.VISIBLE
                            shark.bringToFront()
                        }
                        Thread.sleep(ThreadLocalRandom.current().nextInt(5000, 6000).toLong())
                    } catch (e: Exception) {
                        Log.d("ThreadSleepError", e.toString())
                    }
                }
            }
        }.start()
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.music)
            mediaPlayer!!.setVolume(50f, 50f)
            mediaPlayer!!.isLooping = true
            mediaPlayer!!.start()
        }
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
        if (user.money.value > BigInteger.ZERO) {
            updateMoneyTextView(
                BigInteger.valueOf(percentToAddAfterShakeEvent.toLong()).divide(user.money.value)
            )
            percentToAddAfterShakeEvent = 1
        }
    }

    fun updateMoneyTextView(valueToAdd: BigInteger) {
        user.money.value = user.money.value.add(valueToAdd)
        moneyTextView.text = user.money.toString()
        if (user.money.value.compareTo(
                BigInteger.TEN.pow(user.level * 3)
            ) == 1
        ) {
            user.level++
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
                    }
                )
            )
        }
    }

    override fun onPause() {
        user.saveData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        boatManager.saveData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        super.onPause()
    }

    override fun onResume() {
        user.createValuesFromPref(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        boatManager.createBoatData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        super.onResume()
    }

    override fun onRestart() {
        user.saveData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        boatManager.saveData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        super.onRestart()
    }

    override fun onDestroy() {
        user.saveData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        boatManager.saveData(getSharedPreferences("Preferences", Context.MODE_PRIVATE))
        super.onDestroy()
    }
}
