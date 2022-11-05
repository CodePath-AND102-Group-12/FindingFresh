package com.cpg12.findingfresh.fragments

import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.activities.MainActivity
import com.cpg12.findingfresh.database.Markets
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        supportMapFragment?.getMapAsync(this)



        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val geocoder = Geocoder(view?.context)

        val firestore = FirebaseFirestore.getInstance()
        val docRef = firestore.collection("farms")
        val farmsData = ArrayList<Markets>()

        // helpers to determine whether to issue notification
        var marketToday = false
        var marketsForToday = 0
        lateinit var marketToVisit : Markets
        val today =
            Calendar.getInstance().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
                ?.lowercase()

        docRef.get().addOnSuccessListener { documents ->
            // add each of the markets to the list and create map markers for them
            for (document in documents) {
                val farm = document.toObject(Markets::class.java)
                farmsData.add(farm)
                val geoCoderResults = geocoder.getFromLocationName(farm.marketLocation, 1)
                val marketDetailLatitude = geoCoderResults[0].latitude
                val marketDetailLongitude = geoCoderResults[0].longitude

                if(checkDate(today, farm)) {
                    marketToVisit = farm
                    marketToday = true
                    marketsForToday++
                }

                farm.marketLocation?.let {
                    val loc = LatLng (marketDetailLatitude, marketDetailLongitude)
                    mMap.addMarker(MarkerOptions().position(loc).title(farm.marketName))
                }
            }

            // use the last market added to determine the focus for the map
            val lastMarket = farmsData.first { it.marketName!!.startsWith("Marando")}
            val lastMarketGCR = geocoder.getFromLocationName(lastMarket.marketLocation, 1)
            val lastMarketLatLng = LatLng(lastMarketGCR[0].latitude, lastMarketGCR[0].longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarketLatLng,11f))

            // Notification varies based on whether one or multiple markets are open for today
            if (marketToday) {
                if (marketsForToday > 1) {
                    var myNotification = (activity as MainActivity?)!!.createNotification("Today's the day!", "Don't miss out on ${marketToVisit.marketName} and more open today.")
                } else {
                    var myNotification = (activity as MainActivity?)!!.createNotification("Today's the day!", "Don't miss out on ${marketToVisit.marketName} open today.")
                }
            }
        }


        mMap.uiSettings.isZoomControlsEnabled = true
    }

    private fun checkDate(today : String?, market: Markets) : Boolean {
        return when (today) {
            "sunday" -> market.sunday == true
            "monday" -> market.monday == true
            "tuesday" -> market.tuesday == true
            "wednesday" -> market.wednesday == true
            "thursday" -> market.thursday == true
            "friday" -> market.friday == true
            "saturday" -> market.saturday == true
            else -> true
        }
    }
}