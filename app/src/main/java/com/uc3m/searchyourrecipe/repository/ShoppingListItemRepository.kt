package com.uc3m.searchyourrecipe.repository

import androidx.lifecycle.LiveData
import com.uc3m.searchyourrecipe.models.ShoppingListItem
import com.uc3m.searchyourrecipe.database.ShoppingListItemDAO

class ShoppingListItemRepository(private val shopppingListItemDAO: ShoppingListItemDAO){
    //Visualizar los ingredientes de la shoppinglist
    val readAll: LiveData<List<ShoppingListItem>> = shopppingListItemDAO.readAll()

    //Añadir un ingrediente a la shoppinglist
    suspend fun addIngredient(shoppingListItem: ShoppingListItem){
        shopppingListItemDAO.addIngredient(shoppingListItem)
    }
    //Eliminar un ingrediente a la shoppinglist
     suspend fun deleteIngredient(shoppingListItem: ShoppingListItem){
      shopppingListItemDAO.deleteIngredient(shoppingListItem)
    }

    //Eliminar un ingrediente a la shoppinglist
    suspend fun deleteAllIngredients(){
        shopppingListItemDAO.deleteAllIngredients()
    }
}