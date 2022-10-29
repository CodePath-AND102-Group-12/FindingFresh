package com.cpg12.findingfresh.fragments

import android.content.Intent
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
import com.cpg12.findingfresh.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MarketDetailFragment() : Fragment() {

    //private lateinit var marketDetailDataArray: ArrayList<String>
    private lateinit var marketImage: ImageView

    private val viewModel: FreshViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_market_detail, container, false)

        marketImage = view.findViewById(R.id.marketImage)
        val marketName: TextView = view.findViewById(R.id.marketName)
        val marketAddress: TextView = view.findViewById(R.id.marketAddress)
        val marketCategory: TextView = view.findViewById(R.id.marketCategory)
        val marketPhone: TextView = view.findViewById(R.id.marketPhone)
        val favbtn = view.findViewById<Button>(R.id.favbtn)

        // create market object based on the data array
        val marketDetail = viewModel.market.value

        // use that data to display in the fragment
        marketName.text = marketDetail?.name
        marketAddress.text = marketDetail?.address
        marketCategory.text = marketDetail?.category
        marketPhone.text = marketDetail?.phone

        // This should be replaced by whatever the actual image URL is instead of these generic images
        when (marketDetail?.image) {
            "produce" -> marketImage.setImageResource(R.drawable.produce)
            "bakery" -> marketImage.setImageResource(R.drawable.bakery)
            "fruits" -> marketImage.setImageResource(R.drawable.fruits)
            "plants" -> marketImage.setImageResource(R.drawable.plants)
        }

        //TODO: set to proper address when Market Object is finalized
        marketAddress.setOnClickListener {
            val gmmIntentUri =
                Uri.parse("google.navigation:q=${marketAddress.text}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        favbtn.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val newFavorite: DocumentReference =
                    FirebaseFirestore.getInstance()
                        .collection("users").document(user.uid)
                        .collection("favorites").document()
                val data = hashMapOf("name" to marketDetail?.name,
                    "address" to marketDetail?.address,
                    "category" to marketDetail?.category,
                    "phone" to marketDetail?.phone)
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

}