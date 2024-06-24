package com.example.poiskbiletov

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.poiskbiletov.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bottomNavigation.setOnNavigationItemSelectedListener {  item ->
          when(item.itemId){
              R.id.navigation_tickets ->{
                  openFragment(TicketsFragment())
                  true
              }
              R.id.navigation_hotels ->{
                  openFragment(HotelsFragment())
                  true
              }
              R.id.navigation_shortcut ->{
                  openFragment(ShortcutFragment())
                  true
              }
              R.id.navigation_subscriptions ->{
                  openFragment(SubscriptionsFragment())
                  true
              }
              R.id.navigation_profile ->{
                  openFragment(ProfileFragment())
                  true
              }
              else ->false
          }
      }
        if (savedInstanceState == null){
            binding.bottomNavigation.selectedItemId = R.id.navigation_tickets
        }

    }
    private fun openFragment(fragment:Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    fun showSearchFragment() {
        openFragment(SearchFragment())
    }
}