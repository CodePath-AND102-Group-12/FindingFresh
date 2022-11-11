package com.cpg12.findingfresh.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

// requests for permissions for fine and coarse location to display the blue dot for the current location on the map fragment so that user can see where they are in relation to the markets
class MapFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {
    private var permissionDenied = false
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
        googleMap.setOnMyLocationButtonClickListener(this)
        googleMap.setOnMyLocationClickListener(this)
        enableMyLocation()

        // cheating to move camera away from Africa right away
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(26.1187555,-80.176346),11f))

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

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
//        if (ActivityCompat.shouldShowRequestPermissionRationale(
//                requireActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) || ActivityCompat.shouldShowRequestPermissionRationale(
//                requireActivity(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        ) {
//            PermissionUtils.RationaleDialog.newInstance(
//                LOCATION_PERMISSION_REQUEST_CODE, true
//            ).show(supportFragmentManager, "dialog")
//            return
//        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onMyLocationButtonClick(): Boolean {
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }

        if (isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions,
                grantResults,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation()
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true
        }
    }


    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        /**
         * Checks if the result contains a [PackageManager.PERMISSION_GRANTED] result for a
         * permission from a runtime permissions request.
         *
         * @see androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
         */
        @JvmStatic
        fun isPermissionGranted(
            grantPermissions: Array<String>, grantResults: IntArray,
            permission: String
        ): Boolean {
            for (i in grantPermissions.indices) {
                if (permission == grantPermissions[i]) {
                    return grantResults[i] == PackageManager.PERMISSION_GRANTED
                }
            }
            return false
        }
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