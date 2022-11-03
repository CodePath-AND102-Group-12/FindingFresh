package com.cpg12.findingfresh.fragments

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.database.Markets
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore


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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val geocoder = Geocoder(view?.context)

        val firestore = FirebaseFirestore.getInstance()
        val docRef = firestore.collection("farms")
        val farmsData = ArrayList<Markets>()

        docRef.get().addOnSuccessListener { documents ->
            // add each of the markets to the list and create map markers for them
            for (document in documents) {
                val farm = document.toObject(Markets::class.java)
                farmsData.add(farm)
                println("Added ${farm.marketName} to farmsdata list")
                val geoCoderResults = geocoder.getFromLocationName(farm?.marketLocation, 1)
                val marketDetailLatitude = geoCoderResults.get(0).latitude
                val marketDetailLongitude = geoCoderResults.get(0).longitude

                farm?.marketLocation?.let {
                    val loc = LatLng (marketDetailLatitude, marketDetailLongitude)
                    mMap.addMarker(MarkerOptions().position(loc).title(farm.marketName))
                    println("Market ${farm.marketName} added to map")
                }
            }

            // use the last market added to determine the focus for the map
            val lastMarket = farmsData[farmsData.size - 1]
            val lastMarketGCR = geocoder.getFromLocationName(lastMarket?.marketLocation, 1)
            val lastMarketLatLng = LatLng(lastMarketGCR.get(0).latitude, lastMarketGCR.get(0).longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarketLatLng,10f))
        }

        mMap.uiSettings.isZoomControlsEnabled = true
    }
}