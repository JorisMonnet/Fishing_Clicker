package hearc.dev_mobile.fishing_clicker

import android.os.Bundle
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
import java.math.BigInteger

class MainActivity : AppCompatActivity() {
private lateinit var toggle: ActionBarDrawerToggle
    var generalMoney = Money()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        val contentLayout : FragmentContainerView = findViewById(R.id.FragmentContainerView)
        updateMoneyTextView(BigInteger.ZERO)
        contentLayout.setOnClickListener {
            updateMoneyTextView(BigInteger.valueOf(1))
        }

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        BoatManager.createBoatMenuListener(nav_view,applicationContext)

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
        nav_view.menu.findItem(R.id.boat1).title="Boat1 cost ${BoatManager.initPriceBoat}$"
        return true
    }

    fun updateMoneyTextView(valueToAdd : BigInteger){
        val text : TextView = findViewById(R.id.moneyTextView)
        generalMoney.value = generalMoney.value.add(valueToAdd)
        text.text = generalMoney.toString()
    }
}
