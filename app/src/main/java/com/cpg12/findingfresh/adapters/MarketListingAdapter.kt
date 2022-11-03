package com.cpg12.findingfresh.adapters

import android.content.Context
import android.content.res.Resources
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
import com.cpg12.findingfresh.database.Markets
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class MarketListingAdapter(private var marketList: ArrayList<Markets>,
                           private val context: Context,
                           private val listener: ClickListener
                           )
    :RecyclerView.Adapter<MarketListingAdapter.ViewHolder>(), Filterable {

    var copyMarketList = marketList
    var filterList = ArrayList<Markets>()
    var filterListDays = ArrayList<Markets>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.market_layout, parent,false)
        return ViewHolder(view)
    }

    interface ClickListener {
        fun gotoMarketDetail(position: Int, marketData: Markets)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var marketName: TextView
        var marketImage: ImageView
        init {
            marketName = itemView.findViewById(R.id.marketName)
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

        holder.marketName.text = market.marketName
        val storageReference = FirebaseStorage.getInstance().reference.child("Markets/${market.DocumentId}")
        // val storageReference = FirebaseStorage.getInstance().getReference(market.marketName.toString())
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
                    filterList = copyMarketList as ArrayList<Markets>
                } else {
                    val resultList = ArrayList<Markets>()
                    for (row in marketList) {
                        if (row.marketName!!.lowercase().contains(charSearch.lowercase())) {
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
//                filterList = results?.values as ArrayList<Markets>
                marketList = filterList
                notifyDataSetChanged()
            }
        }
    }

    // to filter based on Day of Week dropdown/spinner
    fun filterSpinner(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charText = p0.toString()
                println("Filter showing $charText")
                if (charText.length == 0) {
                    filterListDays = copyMarketList as ArrayList<Markets>
                } else {
                    var resultList = ArrayList<Markets>()

                    for (each in copyMarketList) {
                        when(charText){
                            "All" -> {
                                resultList = copyMarketList
                            }
                            "Sunday" -> {
                                if (each.sunday == true) {
                                    resultList.add(each)
                                }
                            }
                            "Monday" -> {
                                if (each.monday == true) {
                                    resultList.add(each)
                                }
                            }
                            "Tuesday" -> {
                                if (each.tuesday == true) {
                                    resultList.add(each)
                                }
                            }
                            "Wednesday" -> {
                                if (each.wednesday == true) {
                                    resultList.add(each)
                                }
                            }
                            "Thursday" -> {
                                if (each.thursday == true) {
                                    resultList.add(each)
                                }
                            }
                            "Friday" -> {
                                if (each.friday == true) {
                                    resultList.add(each)
                                }
                            }
                            "Saturday" -> {
                                if (each.saturday == true) {
                                    resultList.add(each)
                                }
                            }
                        }
                    }
                    filterListDays = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterListDays
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                marketList = filterListDays
                notifyDataSetChanged()
            }
        }
    }
}