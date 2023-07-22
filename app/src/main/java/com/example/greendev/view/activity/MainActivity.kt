package com.example.greendev.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.greendev.BindingActivity
import com.example.greendev.R
import com.example.greendev.databinding.ActivityMainBinding
import com.example.greendev.view.fragment.CampaignFragment
import com.example.greendev.view.fragment.MainFragment
import com.example.greendev.view.fragment.ProfileFragment
import com.example.greendev.view.fragment.SearchFragment

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
                    R.id.tab_home -> changeFragment<MainFragment>()
                    R.id.tab_campaign -> changeFragment<CampaignFragment>()
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