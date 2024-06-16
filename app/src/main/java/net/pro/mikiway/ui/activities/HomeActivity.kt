package net.pro.mikiway.ui.activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import net.pro.mikiway.R
import net.pro.mikiway.databinding.ActivityHomeBinding
import net.pro.mikiway.ui.fragments.HomeFragment
import net.pro.mikiway.ui.fragments.LiveFragment
import net.pro.mikiway.ui.fragments.NotificationFragment
import net.pro.mikiway.ui.fragments.PersonFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var homeBinding : ActivityHomeBinding
    private lateinit var nav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        //navigationBar color
        window.navigationBarColor = Color.BLACK


        homeBinding.bottomNavigation.itemBackground
        nav = homeBinding.bottomNavigation
        replaceFragment(HomeFragment())
        setupBottomNavigation()

        onBackPressedDispatcher.addCallback(this) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }
    }


    private fun setupBottomNavigation() {
        nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_home -> replaceFragment(HomeFragment())
                R.id.itemLive -> replaceFragment(LiveFragment())
                R.id.itemNotification -> replaceFragment(NotificationFragment())
                R.id.itemPersonal -> replaceFragment(PersonFragment())

                else -> replaceFragment(HomeFragment())
            }
            updateMenuItemIconTint(item)
            true
        }
    }

    private fun updateMenuItemIconTint(item: MenuItem) {
        val menu = nav.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            val icon = menuItem.icon
            if (menuItem.itemId == item.itemId) {
                icon?.setTintList(ColorStateList.valueOf(getColor(R.color.light_yellow)))
            } else {
                icon?.setTintList(null)
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }
}