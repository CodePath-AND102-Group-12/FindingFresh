package com.cpg12.findingfresh.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.database.ShoppingList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ShoppingListAdapter : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>() {

    private val shoppingList = ArrayList<ShoppingList>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_list_item, parent, false)
        return ShoppingListViewHolder(itemView)
    }


    /**
     * This lets us "bind" each Views in the ViewHolder to its' actual data!
     */
    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        val currentItem = shoppingList[position]
        holder.sListItem.text = currentItem.shoppingItem


        if (currentItem.complete == true){
            holder.sListItem.paintFlags = holder.sListItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.sListItemBox.isChecked = true
        }
    }



    override fun getItemCount(): Int {
        return shoppingList.size
    }

    fun updateShoppingList(shoppingList: List<ShoppingList>){
        this.shoppingList.clear()
        this.shoppingList.addAll(shoppingList)
        notifyDataSetChanged()
    }


    /**
     * This class lets us refer to all the different View elements
     * (Yes, the same ones as in the XML layout files!)
     */
   inner class ShoppingListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        /** object for firebase authentication**/
        private val auth = FirebaseAuth.getInstance()

        /** References the unique id of the current user that is logged in**/
        private val uid = auth.currentUser?.uid

        /** References the node to which market data is stored**/
        private val databaseReference = FirebaseDatabase.getInstance().getReference("Users")


        val sListItem : TextView = itemView.findViewById(R.id.ShoppingItemTV)
        val sListItemBox : CheckBox = itemView.findViewById(R.id.checkBox)

        init{
            itemView.setOnClickListener(this)
            sListItemBox.setOnClickListener {
                strikeOrNot()
            }
        }

        override fun onClick(v: View?) {
            strikeOrNot()
        }

        fun strikeOrNot() {

            // if the text is not having strike then set strike else vice versa
            if (!sListItem.paint.isStrikeThruText) {
                sListItem.paintFlags = sListItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                sListItemBox.isChecked = true
                if (uid != null) {
                    databaseReference.child(uid).child(sListItem.text.toString()).updateChildren(mapOf("complete" to true))
                }
            } else {
                sListItem.paintFlags = sListItem.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                sListItemBox.isChecked = false
                if (uid != null) {
                    databaseReference.child(uid).child(sListItem.text.toString()).updateChildren(mapOf("complete" to false))
                }
            }
        }
    }
}