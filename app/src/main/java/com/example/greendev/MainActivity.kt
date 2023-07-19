package com.example.greendev

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.greendev.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainFragment: Fragment = MainFragment()
    private val campaignFragment: Fragment = CampaignFragment()
    private val searchFragment: Fragment = SearchFragment()
    private val profileFragment: Fragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigationBar()
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        val dialog = FinishDialog(this)
        dialog.initDialog()
        return
    }

    private fun initNavigationBar() {
        binding.bottomNavigationView.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.tab_home -> {
                        changeFragment(mainFragment)
                    }
                    R.id.tab_campaign -> {
                        changeFragment(campaignFragment)
                    }
                    R.id.tab_search -> {
                        changeFragment(searchFragment)
                    }
                    R.id.tab_profile -> {
                        changeFragment(profileFragment)
                    }
                }
                true
            }
            selectedItemId = R.id.tab_home
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}