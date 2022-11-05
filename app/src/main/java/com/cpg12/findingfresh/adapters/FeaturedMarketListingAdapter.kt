package com.cpg12.findingfresh.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.GlideApp
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.database.Markets
import com.cpg12.findingfresh.objects.Market
import com.google.firebase.storage.FirebaseStorage


class FeaturedMarketListingAdapter(private var featuredMarketList: List<Markets>,
                                   private val context: Context,
                                   private val listener: MarketListingAdapter.ClickListener
)
    : RecyclerView.Adapter<FeaturedMarketListingAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.featured_market_layout, parent,false)
        return ViewHolder(view)
    }

    interface ClickListener {
        fun gotoMarketDetail(position: Int, marketData: Markets)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var marketImage: ImageView
        init {
            marketImage = itemView.findViewById(R.id.marketImage)
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position : Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val marketData = featuredMarketList[position]
                listener.gotoMarketDetail(position, marketData)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val market = featuredMarketList[position]

        val storageReference = FirebaseStorage.getInstance().reference.child("Markets/${market.DocumentId}")
        // val storageReference = FirebaseStorage.getInstance().getReference(market.marketName.toString())
        storageReference.downloadUrl.addOnSuccessListener {
            GlideApp.with(context).load(it).into(holder.marketImage)
        }.addOnFailureListener {

        }
    }

    override fun getItemCount(): Int {
        return featuredMarketList.size
    }
}