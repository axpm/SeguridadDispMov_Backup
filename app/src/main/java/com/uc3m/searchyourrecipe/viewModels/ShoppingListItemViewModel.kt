package com.uc3m.searchyourrecipe.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.uc3m.searchyourrecipe.models.ShoppingListItem
import com.uc3m.searchyourrecipe.database.ShoppingListItemDatabase
import com.uc3m.searchyourrecipe.repository.ShoppingListItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingListItemViewModel (application: Application): AndroidViewModel(application) {

    val readAll: LiveData<List<ShoppingListItem>>
    private val repository: ShoppingListItemRepository

    init {
        val shoppingListItemDAO = ShoppingListItemDatabase.getDatabase(application).shoppingListItemDAO()
        repository = ShoppingListItemRepository(shoppingListItemDAO)
        readAll = repository.readAll
    }

    fun addIngredient(shoppingListItem: ShoppingListItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addIngredient(shoppingListItem)
        }
    }

    fun deleteIngredient(shoppingListItem: ShoppingListItem){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteIngredient(shoppingListItem)
        }
    }

    fun deleteAllIngredients(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllIngredients()
        }
    }

}