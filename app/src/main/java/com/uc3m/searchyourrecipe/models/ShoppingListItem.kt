package com.uc3m.searchyourrecipe.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_list_item_table")
data class ShoppingListItem(
        @PrimaryKey
        val nameIngredient: String
)
