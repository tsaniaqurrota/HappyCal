package com.example.uas_happycal.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.uas_happycal.auth.TabAdapter
import com.example.uas_happycal.databinding.ActivityAuthBinding
import com.google.android.material.tabs.TabLayoutMediator

class AuthActivity : AppCompatActivity() {


    lateinit var mediator: TabLayoutMediator
    lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            viewPager.adapter = TabAdapter(supportFragmentManager, this@AuthActivity.lifecycle)
            viewPager2 = viewPager
            mediator = TabLayoutMediator(tabLayout, viewPager)
            { tab, position ->
                when (position) {
                    0 -> tab.text = "Register"
                    1 -> tab.text = "Login"
                }
            }
            mediator.attach()

        }
    }
}