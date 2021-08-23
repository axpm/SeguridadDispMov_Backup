package com.uc3m.searchyourrecipe.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipe_table")
data class FavouriteRecipe(
    @PrimaryKey(autoGenerate = false)
    val id: String, // uri
    val title: String, // label
    val img: String, //image --> link a la imagen
    val time: Int //totalTime --> tiempo en minutos
)