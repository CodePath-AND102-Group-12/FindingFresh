package com.cpg12.findingfresh.objects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cpg12.findingfresh.database.ShoppingList
import com.cpg12.findingfresh.objects.ShoppingListRepository

class ShoppingListViewModel  : ViewModel() {

    private val shoppingListRepository : ShoppingListRepository
    private val _allShoppingLists = MutableLiveData<List<ShoppingList>>()
    val allShoppingList : LiveData<List<ShoppingList>> = _allShoppingLists

    init {
        shoppingListRepository = ShoppingListRepository().getInstance()
        shoppingListRepository.loadShoppingList(_allShoppingLists)
    }



}