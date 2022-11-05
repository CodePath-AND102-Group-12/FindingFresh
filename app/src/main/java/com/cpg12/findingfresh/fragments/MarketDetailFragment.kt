package com.cpg12.findingfresh.fragments

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cpg12.findingfresh.FreshViewModel
import com.cpg12.findingfresh.GlideApp
import com.cpg12.findingfresh.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MarketDetailFragment : Fragment(), OnMapReadyCallback {

    //private lateinit var marketDetailDataArray: ArrayList<String>
    private lateinit var marketImage: ImageView

    private val viewModel: FreshViewModel by activityViewModels()
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_market_detail, container, false)

        marketImage = view.findViewById(R.id.marketImage)
        val marketName: TextView = view.findViewById(R.id.marketName)
        val marketAddress: TextView = view.findViewById(R.id.marketAddress)
        val marketEmail: TextView = view.findViewById(R.id.marketEmail)
        val marketHours: TextView = view.findViewById(R.id.marketHours)
        val marketDescription: TextView = view.findViewById(R.id.marketDescription)
        val marketImage: ImageView = view.findViewById(R.id.marketImage)
        val favBtn = view.findViewById<Button>(R.id.favBtn)

        // create market object based on the data array
        val marketDetail = viewModel.market.value

        // use that data to display in the fragment
        marketName.text = marketDetail?.marketName
        marketAddress.text = marketDetail?.marketLocation
        marketHours.text = "${marketDetail?.marketOpenTime} to ${marketDetail?.marketCloseTime}"
        marketEmail.text = marketDetail?.marketEmail
        marketDescription.text = marketDetail?.marketDescription

        if (marketDetail?.sunday == true) {
            view.findViewById<Chip>(R.id.sunday).visibility = View.VISIBLE
        }

        if (marketDetail?.monday == true) {
            view.findViewById<Chip>(R.id.monday).visibility = View.VISIBLE
        }

        if (marketDetail?.tuesday == true) {
            view.findViewById<Chip>(R.id.tuesday).visibility = View.VISIBLE
        }

        if (marketDetail?.wednesday == true) {
            view.findViewById<Chip>(R.id.wednesday).visibility = View.VISIBLE
        }

        if (marketDetail?.thursday == true) {
            view.findViewById<Chip>(R.id.thursday).visibility = View.VISIBLE
        }

        if (marketDetail?.friday == true) {
            view.findViewById<Chip>(R.id.friday).visibility = View.VISIBLE
        }

        if (marketDetail?.saturday == true) {
            view.findViewById<Chip>(R.id.saturday).visibility = View.VISIBLE
        }

        marketEmail.setOnClickListener {
            val emailTo = marketDetail?.marketEmail
            val subject = "Finding Fresh Inquiry"
            val body = "\n" +
                    "\n" +
                    "------\n" +
                    "I found your farmers market using Finding Fresh!"

            val selectorIntent = Intent(Intent.ACTION_SENDTO)
            val urlString = "mailto:" + Uri.encode(emailTo) + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body)
            selectorIntent.data = Uri.parse(urlString)

            val emailIntent = Intent(Intent.ACTION_MAIN)
            emailIntent.addCategory(Intent.CATEGORY_APP_EMAIL)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailTo))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            emailIntent.putExtra(Intent.EXTRA_TEXT, body)
            emailIntent.selector = selectorIntent

            startActivity(Intent.createChooser(emailIntent,"Email"))
        }


        val storageReference = FirebaseStorage.getInstance().reference.child("Markets/${marketDetail?.DocumentId}")
        // val storageReference = FirebaseStorage.getInstance().getReference(market.marketName.toString())
        storageReference.downloadUrl.addOnSuccessListener {
            GlideApp.with(this).load(it).into(marketImage)
        }.addOnFailureListener {

        }


        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.detailMap) as SupportMapFragment?
        supportMapFragment?.getMapAsync(this)



        //TODO: set to proper address when Market Object is finalized
        marketAddress.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${marketDetail?.marketLocation}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        favBtn.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val newFavorite: DocumentReference =
                    FirebaseFirestore.getInstance()
                        .collection("users").document(user.uid)
                        .collection("favorites").document()
                val data = hashMapOf("name" to marketDetail?.marketName,
                    "address" to marketDetail?.marketLocation,)
                newFavorite.set(data)
            }
            else {
                Toast.makeText(
                    this@MarketDetailFragment.requireContext(),
                    "User not found. Please sign in again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val marketDetail = viewModel.market.value

        val geocoder = Geocoder(view?.context)
        val geoCoderResults = geocoder.getFromLocationName(marketDetail?.marketLocation, 1)
        val marketDetailLatitude = geoCoderResults[0].latitude
        val marketDetailLongitude = geoCoderResults[0].longitude

        marketDetail?.marketLocation?.let {
            val loc = LatLng (marketDetailLatitude, marketDetailLongitude)
            mMap.addMarker(MarkerOptions().position(loc).title(marketDetail.marketName))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,12f))
            mMap.uiSettings.isZoomControlsEnabled = true
        }


    }

}