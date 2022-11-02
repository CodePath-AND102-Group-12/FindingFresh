package com.cpg12.findingfresh.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.GlideApp
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.objects.Market
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class MarketListingAdapter(private var marketList: ArrayList<Market>,
                           private val context: Context,
                           private val listener: ClickListener
                           )
    :RecyclerView.Adapter<MarketListingAdapter.ViewHolder>(), Filterable {

    var copyMarketList = marketList
    var filterList = ArrayList<Market>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.market_layout, parent,false)
        return ViewHolder(view)
    }

    interface ClickListener {
        fun gotoMarketDetail(position: Int, marketData: Market)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var marketName: TextView
        var marketCategory: TextView
        var marketImage: ImageView
        init {
            marketName = itemView.findViewById(R.id.marketName)
            marketCategory = itemView.findViewById(R.id.marketCategory)
            marketImage = itemView.findViewById(R.id.marketImage)
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position : Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val marketData = marketList[position]
                listener.gotoMarketDetail(position, marketData)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = marketList[position]

        holder.marketName.text = market.name
        holder.marketCategory.text = market.category
        val storageReference = FirebaseStorage.getInstance().getReference(market.image.toString())
        storageReference.downloadUrl.addOnSuccessListener {
            GlideApp.with(context).load(it).into(holder.marketImage)
        }.addOnFailureListener {

        }
    }

    override fun getItemCount(): Int {
        return marketList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = copyMarketList as ArrayList<Market>
                } else {
                    val resultList = ArrayList<Market>()
                    for (row in marketList) {
                        if (row.name!!.lowercase().contains(charSearch.lowercase())) {
                            resultList.add(row)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                filterList = results?.values as ArrayList<Market>
                marketList = filterList
                notifyDataSetChanged()
            }
        }
    }
}