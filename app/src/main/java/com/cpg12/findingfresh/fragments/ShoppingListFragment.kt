package com.cpg12.findingfresh.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cpg12.findingfresh.R
import com.cpg12.findingfresh.adapters.ShoppingListAdapter
import com.cpg12.findingfresh.database.ShoppingList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference


class ShoppingListFragment : Fragment() {
    private lateinit var shoppingRecyclerView: RecyclerView
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private lateinit var shoppingArrayList: ArrayList<ShoppingList>

    /** Firebase object initialization **/
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    /*
     * Constructing the view
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shopping_list, container, false)

       // val shoppingItem: TextView = view.findViewById(R.id.ShoppingItemTV)
        val shoppingField: EditText = view.findViewById(R.id.yourItemET)
        val shoppingButton: Button = view.findViewById(R.id.submitItemBtn)

        val shoppingRecyclerView = view.findViewById(R.id.shoppingRV) as RecyclerView
        shoppingRecyclerView.layoutManager = GridLayoutManager(context, 1) //controls movies per row
        shoppingArrayList = arrayListOf()
        shoppingListAdapter = ShoppingListAdapter(shoppingArrayList)
        shoppingRecyclerView.adapter = shoppingListAdapter

        //EventChangeListener()

        /** object for firebase authentication**/
        auth = FirebaseAuth.getInstance()

        /** References the unique id of the current user that is logged in**/
        val uid = auth.currentUser?.uid

        /** References the node to which market data is stored**/
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")


        /** Creates the "shoppingList" node under the UID, to store shopping list**/
        shoppingButton.setOnClickListener {

            /** Gets the string value from field **/
            val itemToAdd = shoppingField.text.toString()
            /** create instance of ShoppingList class **/
            val shoppingList = ShoppingList(itemToAdd)


            if (uid != null) {
                databaseReference.child(uid).child("shoppingList").push().setValue(shoppingList).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(activity, "Shopping Item added", Toast.LENGTH_SHORT).show()
                        shoppingField.setText("")
                    } else{
                        Toast.makeText(activity, "Unable to add shopping item", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return view
    }

/*    private fun EventChangeListener() {
        val auth = FirebaseAuth.getInstance().currentUser
        var db = FirebaseFirestore.getInstance()
        if (auth != null) {
            db.collection("users")
                .document(auth.uid)
                .collection("Shopping List")
                .document()
                .addSnapshotListener(object : EventListener<DocumentSnapshot> {
                    override fun onEvent(
                        value: DocumentSnapshot?,
                        error: FirebaseFirestoreException?
                    ) {
                        if (error != null){
                            Log.e("Firestore Error", error.message.toString())
                            return
                        }


                    }

                })
        }
    }*/


}