package com.example.fishing_clicker

import android.os.Bundle
import android.view.Menu
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.example.fishing_clicker.boat.Boat
import java.util.*

class MainActivity : AppCompatActivity() {


    private var boatList: MutableList<Boat> = LinkedList<Boat>();
    private var boatIndex = 1;
    private var baseEfficiency = 1.0;


    fun buyABoat(view : View) {
        boatList.add(Boat(baseEfficiency, boatIndex));
        baseEfficiency *= 1.5;
        boatIndex++;
    }


    private lateinit var appBarConfiguration: AppBarConfiguration

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


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

}