package com.cpg12.findingfresh.fragments

import android.os.Bundle
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
import com.google.firebase.database.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cpg12.findingfresh.objects.ShoppingListViewModel


class ShoppingListFragment : Fragment() {
    private lateinit var shoppingRecyclerView: RecyclerView
    private lateinit var shoppingListAdapter: ShoppingListAdapter

    private lateinit var shoppingViewModel: ShoppingListViewModel

    /** Firebase object initialization **/
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("loaded market listing fragment")
    }


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
        val deleteAllButton : Button = view.findViewById(R.id.deleteItemBtn)


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
                databaseReference.child(uid).push().setValue(shoppingList).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(activity, "Shopping Item added", Toast.LENGTH_SHORT).show()
                        shoppingField.setText("")
                    } else{
                        Toast.makeText(activity, "Unable to add shopping item", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        deleteAllButton.setOnClickListener {
            if (uid != null) {
                databaseReference.child(uid).removeValue()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(activity, "Shopping List Deleted", Toast.LENGTH_SHORT)
                                .show()
                            shoppingField.setText("")
                        } else {
                            Toast.makeText(
                                activity,
                                "Unable to remove shopping list",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        return view
    }



        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingRecyclerView = view.findViewById(R.id.shoppingRV)
        shoppingRecyclerView.layoutManager = GridLayoutManager(context, 1)
        shoppingListAdapter = ShoppingListAdapter()
        shoppingRecyclerView.adapter = shoppingListAdapter
        shoppingViewModel = ViewModelProvider(this)[ShoppingListViewModel::class.java]
        shoppingViewModel.allShoppingList.observe(viewLifecycleOwner, Observer {
            shoppingListAdapter.updateShoppingList(it)
        })
    }






}