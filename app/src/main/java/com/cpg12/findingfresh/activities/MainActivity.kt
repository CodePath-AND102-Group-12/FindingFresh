package com.cpg12.findingfresh.activities

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cpg12.findingfresh.FreshViewModel
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.databinding.ActivityMainBinding
import com.cpg12.findingfresh.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // fragment support, each fragment gets initialized
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val marketListFragment: Fragment = MarketListingFragment()
    private val mapFragment: Fragment = MapFragment()
    private val shoppingListFragment: Fragment = ShoppingListFragment()
    private val favoriteMarketsFragment: Fragment = FavoriteMarketsFragment()
    private val settingsFragment: Fragment = SettingsFragment()


    private val CHANNEL_ID = "channelID23"
    private val CHANNEL_NAME = "channelName"
    private val NOTIF_ID = 172356


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val viewRoot = binding.root
        setContentView(viewRoot)

        // messing around with notifications

        createNotifChannel()





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
                R.id.action_goto_favorite_markets -> {
                    fragment = favoriteMarketsFragment
                }
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

    private fun createNotifChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    fun createNotification(contentTitle : String, contentText : String) {

        val intent= Intent(this,MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
        }

        val notif = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(contentTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_account)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notifManger = NotificationManagerCompat.from(this)
        notifManger.notify(NOTIF_ID, notif)
    }

//    fun displayNotification(notif: Notification) {
//    }

}