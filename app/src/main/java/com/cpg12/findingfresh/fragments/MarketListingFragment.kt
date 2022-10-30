package com.cpg12.findingfresh.fragments

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.FreshViewModel
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.adapters.FeaturedMarketListingAdapter
import com.cpg12.findingfresh.adapters.MarketListingAdapter
import com.cpg12.findingfresh.database.MarketFetcher
import com.cpg12.findingfresh.objects.Market
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MarketListingFragment : Fragment(), MarketListingAdapter.ClickListener {
    private lateinit var featuredMarketListingAdapter: FeaturedMarketListingAdapter
    private lateinit var marketList: List<Market>

    private val viewModel: FreshViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("loaded market listing fragment")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_market_listing, container, false)
        val featuredMarketListLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val featuredMarketListRecyclerView =
            view.findViewById<RecyclerView>(R.id.featuredMarketsRecyclerView)

        val firestore = FirebaseFirestore.getInstance()
        val docRef = firestore.collection("farms")
        docRef.get().addOnSuccessListener { documents ->
            val farmsData = mutableListOf<Market>()
            var i = 0
            for (document in documents) {
                val farm = document.toObject(Market::class.java)
                farmsData.add(farm)
                println(farmsData[i].name)
                i++
            }
            println(farmsData[0].name)
            val allMarketListingAdapter = MarketListingAdapter(
                farmsData,
                requireContext(),
                this
            )
            val allMarketListLayoutManager = GridLayoutManager(context, 2)
            val allMarketListRecyclerView = view.findViewById<RecyclerView>(R.id.allMarketsRecyclerView)
            allMarketListRecyclerView.adapter = allMarketListingAdapter
            allMarketListRecyclerView.layoutManager = allMarketListLayoutManager
        }

        /** still using marketfetcher for featured markets! **/
        val marketList = MarketFetcher.getItems()

        featuredMarketListingAdapter = FeaturedMarketListingAdapter(marketList.subList(0,marketList.size - 1))
        featuredMarketListRecyclerView.adapter = featuredMarketListingAdapter
        featuredMarketListRecyclerView.layoutManager = featuredMarketListLayoutManager

        // default spinner code based on official documentation: https://developer.android.com/develop/ui/views/components/spinner
        // will need to later implement an OnItemSelectedListener
        val spinner: Spinner = view.findViewById(R.id.spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        activity?.let {
            ArrayAdapter.createFromResource(
                it.applicationContext,
                R.array.daysOfWeek,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }


        return view
    }


    companion object {
        fun newInstance(): MarketListingFragment {
            return MarketListingFragment()
        }
    }

    override fun gotoMarketDetail(position: Int, marketData: Market) {
        viewModel.setMarketData(marketData)
        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, MarketDetailFragment())
        transaction.addToBackStack(null)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

}