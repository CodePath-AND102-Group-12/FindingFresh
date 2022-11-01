package com.cpg12.findingfresh.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.database.ShoppingList
import com.google.firebase.database.FirebaseDatabase

class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    private val shoppingList = ArrayList<ShoppingList>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_list_item, parent, false)

        /** References the node to which market data is stored**/
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        return ShoppingListViewHolder(itemView)
    }


    /**
     * This lets us "bind" each Views in the ViewHolder to its' actual data!
     */
    override fun onBindViewHolder(holder: ShoppingListAdapter.ShoppingListViewHolder, position: Int) {
        val currentItem = shoppingList[position]
        holder.sListItem.text = currentItem.shoppingItem

    }



    override fun getItemCount(): Int {
        return shoppingList.size
    }

    fun updateShoppinglist(shoppingList: List<ShoppingList>){
        this.shoppingList.clear()
        this.shoppingList.addAll(shoppingList)
        notifyDataSetChanged()
    }


    /**
     * This class lets us refer to all the different View elements
     * (Yes, the same ones as in the XML layout files!)
     */
    class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val sListItem : TextView = itemView.findViewById(R.id.ShoppingItemTV)

        override fun onClick(v: View?) {
        }
    }
}