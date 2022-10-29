package com.cpg12.findingfresh.activities

import android.R.attr.fragment
import android.R.attr.key
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cpg12.findingfresh.FreshViewModel
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.databinding.ActivityMainBinding
import com.cpg12.findingfresh.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // fragment support, each fragment gets initialized
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val marketListFragment: Fragment = MarketListingFragment()
    private val mapFragment: Fragment = MapFragment()
    private val shoppingListFragment: Fragment = ShoppingListFragment()
    private val calendarFragment: Fragment = CalendarFragment()
    private val settingsFragment: Fragment = SettingsFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val viewRoot = binding.root
        setContentView(viewRoot)

        /** Information passed from intent (registration or login)
         * Appears in the settings textview
         */
        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")


        /** ViewModel **/
        val viewModel: FreshViewModel by viewModels()

        /** Navigation **/
        val nav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        nav.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.action_goto_markets -> fragment = marketListFragment
                R.id.action_goto_map -> fragment = mapFragment
                R.id.action_goto_shopping_list -> fragment = shoppingListFragment
                R.id.action_goto_calendar -> fragment = calendarFragment
                R.id.action_goto_settings -> fragment = settingsFragment
            }
            replaceFragment(fragment)
            true
        }

        // set default as Market Listing fragment
        nav.selectedItemId = R.id.action_goto_markets

        // start the Activity on Market Listing fragment
        replaceFragment(marketListFragment)


    }

    // Helper function to clean up fragment changes
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content, fragment)
        fragmentTransaction.commit()
    }

}