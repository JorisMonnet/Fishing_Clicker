package com.example.fishing_clicker.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fishing_clicker.R


class UpgradeMenu : AppCompatActivity() {
    private var mDrawerList: ListView? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var mAdapter: ArrayAdapter<String>? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mActivityTitle: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val topToolBar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(topToolBar)
        //mDrawerList = findViewById<View>(R.id.navList) as ListView
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        mActivityTitle = title.toString()
        addDrawerItems()
        setupDrawer()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    private fun addDrawerItems() {
        val osArray = arrayOf("Android", "iOS", "Windows", "OS X", "Linux")
        mAdapter = ArrayAdapter(this, R.layout.activity_main, R.id.menu, osArray)
        mDrawerList!!.adapter = mAdapter
        mDrawerList!!.onItemClickListener =
            OnItemClickListener { parent, view, position, id ->
                Toast.makeText(
                    this@UpgradeMenu,
                    "Time for an upgrade!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun setupDrawer() {
        mDrawerToggle = object : ActionBarDrawerToggle(
            this,
            mDrawerLayout,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            /** Called when a drawer has settled in a completely open state.  */
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                supportActionBar!!.setTitle("Navigation!")
                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state.  */
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                supportActionBar!!.setTitle(mActivityTitle)
                invalidateOptionsMenu() // creates call to onPrepareOptionsMenu()
            }
        }
        (mDrawerToggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled = true
        mDrawerLayout!!.setDrawerListener(mDrawerToggle)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_main_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.boat3) {
            Toast.makeText(this@UpgradeMenu, "boat3", Toast.LENGTH_LONG).show()
        }
        if (id == R.id.boat1) {
            Toast.makeText(this@UpgradeMenu, "boat1", Toast.LENGTH_LONG).show()
        }
        if (id == R.id.boat2) {
            Toast.makeText(this@UpgradeMenu, "boat2", Toast.LENGTH_LONG).show()
        }
        // Activate the navigation drawer toggle
        return if (mDrawerToggle?.onOptionsItemSelected(item)!!) {
            true
        } else super.onOptionsItemSelected(item)
    }
}