package com.cpg12.findingfresh.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cpg12.findingfresh.FreshViewModel
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.adapters.FeaturedMarketListingAdapter
import com.cpg12.findingfresh.adapters.MarketListingAdapter
import com.cpg12.findingfresh.database.Markets
import com.google.firebase.firestore.FirebaseFirestore

class MarketListingFragment : Fragment(), MarketListingAdapter.ClickListener {
    private lateinit var featuredMarketListingAdapter: FeaturedMarketListingAdapter
    private lateinit var allMarketListingAdapter: MarketListingAdapter

    // to help prevent loading of spinner from crashing app, based on: https://stackoverflow.com/a/7660052
    private var spinnerCount = 0
    private val spinnerEvents = 1

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

        val firestore = FirebaseFirestore.getInstance()

        // getting list of all markets
        val docRef = firestore.collection("farms")
        docRef.get().addOnSuccessListener { documents ->
            val farmsData = ArrayList<Markets>()
            for (document in documents) {
                val farm = document.toObject(Markets::class.java)
                farmsData.add(farm)
            }
            println(farmsData[0].marketName)
            allMarketListingAdapter = MarketListingAdapter(
                farmsData,
                requireContext(),
                this
            )
            val allMarketListLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            val allMarketListRecyclerView = view.findViewById<RecyclerView>(R.id.allMarketsRecyclerView)
            allMarketListRecyclerView.adapter = allMarketListingAdapter
            allMarketListRecyclerView.layoutManager = allMarketListLayoutManager
        }

        // getting list of featured markets
        val docRefFeatured = firestore.collection("farms").whereEqualTo("featured", true)
        docRefFeatured.get().addOnSuccessListener { documents ->
            val featuredFarmsData = ArrayList<Markets>()
            for (document in documents) {
                val farm = document.toObject(Markets::class.java)
                featuredFarmsData.add(farm)
            }
            println(featuredFarmsData[0].marketName)
            featuredMarketListingAdapter = FeaturedMarketListingAdapter(
                featuredFarmsData,
                requireContext(),
                this
            )
            val featuredMarketListLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val featuredMarketListRecyclerView = view.findViewById<RecyclerView>(R.id.featuredMarketsRecyclerView)
            featuredMarketListRecyclerView.adapter = featuredMarketListingAdapter
            featuredMarketListRecyclerView.layoutManager = featuredMarketListLayoutManager
        }

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


        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, spinnerView: View?, spinnerPosition: Int, spinnerId: Long) {
                if (spinnerCount < spinnerEvents) {
                    spinnerCount++
                } else {
                    val spinnerSelection = spinner.selectedItem.toString()
                    println("Spinner Selection: $spinnerSelection")

                    allMarketListingAdapter.filterSpinner().filter(spinnerSelection)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // required member
            }

        }

        val searchViewField = view.findViewById<SearchView>(R.id.searchViewField)

        searchViewField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                allMarketListingAdapter.filter.filter(query)
                return false
            }

        })


        return view
    }


    companion object {
        fun newInstance(): MarketListingFragment {
            return MarketListingFragment()
        }
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