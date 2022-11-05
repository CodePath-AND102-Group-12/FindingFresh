package com.cpg12.findingfresh.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.database.Markets
import com.cpg12.findingfresh.fragments.FavoriteMarketsFragment


class FavoriteMarketsAdapter(private var marketList: ArrayList<Markets>,
                             private val context: Context,
                             private val listener: FavoriteMarketsFragment
)
    :RecyclerView.Adapter<FavoriteMarketsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.favorite_market_layout, parent,false)
        return ViewHolder(view)
    }

    interface ClickListener {
        fun gotoMarketDetail(position: Int, marketData: Markets)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var marketName: TextView
        var marketAddress: TextView
        init {
            marketName = itemView.findViewById(R.id.marketName)
            marketAddress = itemView.findViewById(R.id.marketAddress)
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

    override fun getItemCount(): Int {
        return marketList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = marketList[position]

        holder.marketName.text = market.marketName
        holder.marketAddress.text = market.marketLocation
    }
}