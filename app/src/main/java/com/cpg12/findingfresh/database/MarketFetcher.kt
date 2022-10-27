package com.cpg12.findingfresh.database

import com.cpg12.findingfresh.objects.Market

class MarketFetcher {
    companion object {
        val name = listOf("Produce Market", "Baked Goods Market", "Fruit Market", "Plant Market")
        val address = listOf("123 Cool St", "234 Main St", "345 First St", "456 Blvd")
        val phone = listOf("123-456-7890", "234-567-8901", "345-678-9012", "456-789-0123")
        val category = listOf("Produce", "Baked Goods", "Jams", "Nuts")
        val image = listOf("produce", "bakery", "fruits", "plants")

        fun getItems(): MutableList<Market> {
            val featuredMarketList : MutableList<Market> = ArrayList()
            for (i in name.indices) {
                val featuredMarket = Market(name[i], address[i], phone[i], category[i], image[i])
                featuredMarketList.add(featuredMarket)
            }
            return featuredMarketList
        }
    }
}