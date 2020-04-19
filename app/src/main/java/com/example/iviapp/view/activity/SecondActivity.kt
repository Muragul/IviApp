package com.example.iviapp.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import com.example.iviapp.*
import com.example.iviapp.view.fragment.FirstFragment
import com.example.iviapp.view.fragment.SecondFragment
import com.example.iviapp.view.fragment.ThirdFragment
import com.example.iviapp.view.pager.LockableViewPager
import com.example.iviapp.view.pager.SlidePagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class SecondActivity : AppCompatActivity() {
    private lateinit var pager: LockableViewPager
    private lateinit var pagerAdapter: PagerAdapter
    private var mainPageFragment: Fragment = FirstFragment()
    private var favoritePageFragment: Fragment = SecondFragment()
    private var profileFragment: Fragment = ThirdFragment()
    private var fragmentList: MutableList<Fragment> = ArrayList()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        fragmentList.add(mainPageFragment)
        fragmentList.add(favoritePageFragment)
        fragmentList.add(profileFragment)
        pager = findViewById(R.id.pager)
        pager.setSwipable(false)
        pagerAdapter = SlidePagerAdapter(supportFragmentManager, fragmentList)
        pager.adapter = pagerAdapter

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    pager.setCurrentItem(0, false)
                    bottomNavigationView.menu.findItem(R.id.save)
                        .setIcon(R.drawable.ic_save)
                    bottomNavigationView.menu.findItem(R.id.menu_settings)
                        .setIcon(R.drawable.ic_settings)
                    item.setIcon(R.drawable.ic_home)
                }
                R.id.save -> {
                    pager.setCurrentItem(1, false)
                    item.setIcon(R.drawable.ic_favorite)
                    bottomNavigationView.menu.findItem(R.id.menu_settings)
                        .setIcon(R.drawable.ic_settings)
                    bottomNavigationView.menu.findItem(R.id.home)
                        .setIcon(R.drawable.ic_home_new)
                }
                R.id.menu_settings -> {
                    pager.setCurrentItem(2, false)
                    bottomNavigationView.menu.findItem(R.id.save)
                        .setIcon(R.drawable.ic_save)
                    bottomNavigationView.menu.findItem(R.id.home)
                        .setIcon(R.drawable.ic_home_new)
                    item.setIcon(R.drawable.ic_person)
                }
            }
            false
        }

    }
}
