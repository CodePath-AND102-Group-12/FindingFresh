package com.cpg12.findingfresh.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.database.ShoppingList

class ShoppingListAdapter(
    private val shoppingList: List<ShoppingList>,
    )

: RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_shopping_list, parent, false)
        return ShoppingListViewHolder(view)
    }


    /**
     * This inner class lets us refer to all the different View elements
     * (Yes, the same ones as in the XML layout files!)
     */
    inner class ShoppingListViewHolder(val sView: View) : RecyclerView.ViewHolder(sView) {
        val sListItem: TextView = sView.findViewById<View>(R.id.shoppingListTV) as TextView
    }


    /**
     * This lets us "bind" each Views in the ViewHolder to its' actual data!
     */
    override fun onBindViewHolder(holder: ShoppingListAdapter.ShoppingListViewHolder, position: Int) {
        val slist = shoppingList[position]
        holder.sListItem.text = slist.shoppingItem
    }


    override fun getItemCount(): Int {
        return shoppingList.size
    }
}