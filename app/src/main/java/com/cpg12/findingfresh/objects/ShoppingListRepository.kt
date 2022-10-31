package com.cpg12.findingfresh.objects

import androidx.lifecycle.MutableLiveData
import com.cpg12.findingfresh.database.ShoppingList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShoppingListRepository {

    @Volatile private var INSTANCE : ShoppingListRepository?= null

    /** object for firebase authentication**/
    val auth  = FirebaseAuth.getInstance()

    /** References the unique id of the current user that is logged in**/
    val uid = auth.currentUser?.uid

    /** References the node to which market data is stored**/
   private val databaseReference = FirebaseDatabase.getInstance().getReference("Users")

    /* Checks if instance is null */
    fun getInstance() : ShoppingListRepository {
        return INSTANCE ?: synchronized(this){

            val instance = ShoppingListRepository()
            INSTANCE = instance
            instance
        }
    }

    /* Whenever there's a change in the database, will modify shopping list*/
    fun loadShoppingList(shoppingList : MutableLiveData<List<ShoppingList>>){
        if (uid != null) {
            databaseReference.child(uid).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try{
                        val _shoppingList : List<ShoppingList> = snapshot.children.map { dataSnapshot ->
                            dataSnapshot.getValue(ShoppingList::class.java)!!
                        }
                        shoppingList.postValue(_shoppingList)
                    }catch (e: Exception){
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}