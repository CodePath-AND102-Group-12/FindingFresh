package com.cpg12.findingfresh.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.objects.Market

private lateinit var featuredMarketsRVLayout: ConstraintLayout
private lateinit var  marketName: TextView
private lateinit var  marketAddress: TextView
private lateinit var  marketPhone: TextView

class FeaturedMarketListingAdapter(private val featuredMarketList: MutableList<Market>): RecyclerView.Adapter<FeaturedMarketListingAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // TODO: Create member variables for any view that will be set
        // as you render a row.

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each sub-view
        init {
            // TODO: Store each of the layout's views into
            // the public final member variables created above
            featuredMarketsRVLayout = itemView.findViewById(R.id.featuredMarketsRVLayout)
            marketName = itemView.findViewById(R.id.marketName)
            marketAddress = itemView.findViewById(R.id.marketAddress)
            marketPhone = itemView.findViewById(R.id.marketPhone)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val  contactView = inflater.inflate(R.layout.featured_market_layout, parent, false)

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val featuredMarket = featuredMarketList.get(position)

        marketName.text = featuredMarket.name
        marketAddress.text = featuredMarket.address
        marketPhone.text = featuredMarket.phone

        featuredMarketsRVLayout.setOnClickListener {
            Toast.makeText(it.context, "${featuredMarket.name} clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return featuredMarketList.size
    }
}