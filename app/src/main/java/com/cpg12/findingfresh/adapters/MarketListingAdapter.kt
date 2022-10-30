package com.cpg12.findingfresh.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.objects.Market

class MarketListingAdapter(private val marketList: List<Market>,
                           private val context: Context,
                           private val listener: ClickListener
                           )
    :RecyclerView.Adapter<MarketListingAdapter.ViewHolder>() {


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

    }

    override fun getItemCount(): Int {
        return marketList.size
    }
}