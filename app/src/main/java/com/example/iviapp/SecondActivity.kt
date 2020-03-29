package com.example.iviapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_page.*


class SecondActivity : AppCompatActivity() {
    private lateinit var pager: LockableViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private var f1: Fragment = FirstFragment()
    private var f2: Fragment = SecondFragment()
    private var list: MutableList<Fragment> = ArrayList()
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        var toolbar: TextView = findViewById(R.id.toolbar)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        list.add(f1)
        list.add(f2)
        pager = findViewById(R.id.pager)
        pager.setSwipable(false)
        pagerAdapter = SlidePagerAdapter(supportFragmentManager, list)
        pager.adapter = pagerAdapter

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    pager.setCurrentItem(0, false)
                    bottomNavigationView.menu.findItem(R.id.save)
                        .setIcon(R.drawable.ic_save)
                    toolbar.text = "Popular"
                }
                R.id.save -> {
                    pager.setCurrentItem(1, false)
                    item.setIcon(R.drawable.ic_favorite)
                    toolbar.text = "Favorites"
                }
                R.id.menu_settings -> {
                    pager.setCurrentItem(2, false)
                    bottomNavigationView.menu.findItem(R.id.save)
                        .setIcon(R.drawable.ic_save)
                    toolbar.text = "Profile"
                }
            }
            false
        }

    }
}
