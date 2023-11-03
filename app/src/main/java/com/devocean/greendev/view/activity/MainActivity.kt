package com.devocean.greendev.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.devocean.greendev.R
import com.devocean.greendev.databinding.ActivityMainBinding
import com.devocean.greendev.util.BindingActivity
import com.devocean.greendev.view.fragment.CreateCampaignFragment
import com.devocean.greendev.view.fragment.HomeFragment
import com.devocean.greendev.view.fragment.ProfileFragment
import com.devocean.greendev.view.fragment.SearchFragment

class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initNavigationBar()
    }

    private fun initNavigationBar() {
        binding.bottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.tab_home -> changeFragment<HomeFragment>()
                    R.id.tab_campaign -> changeFragment<CreateCampaignFragment>()
                    R.id.tab_search -> changeFragment<SearchFragment>()
                    R.id.tab_profile -> changeFragment<ProfileFragment>()
                }
                true
            }
            selectedItemId = R.id.tab_home
        }
    }

    private inline fun <reified T: Fragment> changeFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, T::class.java.newInstance())
            .commit()
    }
}