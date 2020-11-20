package hearc.dev_mobile.fishing_clicker

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentContainerView
import java.math.BigInteger
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    var user = User()
    private val contentLayout : FragmentContainerView = findViewById(R.id.FragmentContainerView)

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

        updateMoneyTextView(BigInteger.ZERO)
        contentLayout.setOnClickListener {
            updateMoneyTextView(user.getClickValue())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    fun updateMoneyTextView(valueToAdd : BigInteger){
        val text : TextView = findViewById(R.id.moneyTextView)
        user.money.value = user.money.value.add(valueToAdd)
        text.text = user.money.toString()
        if(user.money.value.compareTo(BigInteger.valueOf(10.0.pow(user.level*3+6).toLong()))==1){
            user.level++
            TODO()  //change background when changing of level
            //contentLayout.background = R.drawable.bgLevel1
        }
    }
}