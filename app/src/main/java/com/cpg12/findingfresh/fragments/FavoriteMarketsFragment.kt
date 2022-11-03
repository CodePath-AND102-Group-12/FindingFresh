package com.cpg12.findingfresh.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.adapters.MarketListingAdapter
import com.cpg12.findingfresh.adapters.ShoppingListAdapter
import com.cpg12.findingfresh.database.Markets
import com.cpg12.findingfresh.database.ShoppingList
import com.cpg12.findingfresh.objects.ShoppingListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoriteMarketsFragment : Fragment() {
    private lateinit var favoriteMarketsRV: RecyclerView
//    private lateinit var favoriteMarketsAdapter: FavoriteMarketsAdapter
//    private lateinit var favoriteMarketsList: ArrayList<Markets>

    private lateinit var shoppingViewModel: ShoppingListViewModel

    /** Firebase object initialization **/
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite_markets, container, false)

        val textView = view.findViewById<TextView>(R.id.myOnlyTV)
        var myStringOfMarkets = StringBuilder()

        /** object for firebase authentication**/
        auth = FirebaseAuth.getInstance()

        /** References the unique id of the current user that is logged in**/
        val uid = auth.currentUser?.uid

        /** References the node to which market data is stored**/
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/favorites")

        val db = Firebase.firestore
        val loggedInUserFavorites = db.collection("users").document(uid.toString()).collection("favorites")

        myStringOfMarkets.append("The current user has the following markets in their favorites:")

        loggedInUserFavorites.get().addOnSuccessListener { documents ->
            val favoriteMarkets = ArrayList<String>()
            for (document in documents) {
                val doc = loggedInUserFavorites.document(document.id)
                favoriteMarkets.add(doc.get().toString())
                val name = document.data.get("name")
                val address = document.data.get("address")

                myStringOfMarkets.append("\n\n$name: $address")

                println("$uid favorite markets: ${document.data.get("name")}")
            }

            textView.setText(myStringOfMarkets)
//            allMarketListingAdapter = MarketListingAdapter(
//                favoriteMarkets,
//                requireContext(),
//                this
//            )
//            val allMarketListLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
//            val allMarketListRecyclerView = view.findViewById<RecyclerView>(R.id.allMarketsRecyclerView)
//            allMarketListRecyclerView.adapter = allMarketListingAdapter
//            allMarketListRecyclerView.layoutManager = allMarketListLayoutManager
        }





        return view
    }
}