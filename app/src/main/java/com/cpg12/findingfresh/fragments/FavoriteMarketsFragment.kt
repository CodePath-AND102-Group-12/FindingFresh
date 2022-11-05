package com.cpg12.findingfresh.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.FreshViewModel
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.adapters.FavoriteMarketsAdapter
import com.cpg12.findingfresh.database.Markets
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoriteMarketsFragment : Fragment(), FavoriteMarketsAdapter.ClickListener {
    private lateinit var favoriteMarketsRV: RecyclerView
    private lateinit var favoriteMarketsAdapter: FavoriteMarketsAdapter
    private val viewModel: FreshViewModel by activityViewModels()

    // TODO: get rid of this TextView/myStringOfMarkets. On my emulator testing, if I remove the RV doesn't load
    private lateinit var textView : TextView
    private lateinit var myStringOfMarkets : StringBuilder

    /** Firebase object initialization **/
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("NOW WORKING IN FAVORITE MARKETS FRAGMENT")
        val view = inflater.inflate(R.layout.fragment_favorite_markets, container, false)

        // TODO: get rid of this TextView/myStringOfMarkets. On my emulator testing, if I remove the RV doesn't load
        textView = view.findViewById(R.id.myOnlyTV)
        myStringOfMarkets = StringBuilder()

        /** object for firebase authentication**/
        auth = FirebaseAuth.getInstance()

        /** References the unique id of the current user that is logged in**/
        val uid = auth.currentUser?.uid

        /** References the node to which market data is stored**/
        databaseReference = FirebaseDatabase.getInstance().getReference("Users/favorites")

        val db = Firebase.firestore
        val loggedInUserFavorites = db.collection("users").document(uid.toString()).collection("favorites")

        myStringOfMarkets.append("The current user has the following markets in their favorites:")

        val favoritesAsString = ArrayList<String>()
        val favoriteMarkets = ArrayList<Markets>()

        loggedInUserFavorites.get().addOnSuccessListener { documents ->

            // first iterate through the user to determine what has been added as favorites, add to the favoritesAsString list
            for (document in documents) {
                val name = document.data["name"]
                favoritesAsString.add(name.toString())
            }

            // then using the favoritesAsString list, query Firestore for matching results and add matches to the favoriteMarkets list
            for (each in favoritesAsString) {
                db.collection("farms")
                    .whereEqualTo("marketName", each)
                    .get()
                    .addOnSuccessListener { farms ->
                        // with each one, create the Markets object that we can use later to work with an adapter
                        for (farm in farms) {
                            val favoriteFarm = farm.toObject(Markets::class.java)
                            favoriteMarkets.add(favoriteFarm)

                            myStringOfMarkets.append("${favoriteFarm.marketName?.first()}: ${favoriteFarm.marketLocation?.first()}")
                        }

                        textView.text = myStringOfMarkets
                    }
            }

            favoriteMarketsAdapter = FavoriteMarketsAdapter(
                favoriteMarkets,
                requireContext(),
                this
            )
            val favoriteMarketsLayoutManager = LinearLayoutManager(context)
            favoriteMarketsRV = view.findViewById(R.id.favoriteMarketsRV)
            favoriteMarketsRV.adapter = favoriteMarketsAdapter
            favoriteMarketsRV.layoutManager = favoriteMarketsLayoutManager
        }


        return view
    }


    override fun gotoMarketDetail(position: Int, marketData: Markets) {
        viewModel.setMarketData(marketData)
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, MarketDetailFragment())
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }
}