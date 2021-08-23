package com.uc3m.searchyourrecipe.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient (
    val text: String, //quantity
    val weight: Float
) : Parcelable

@Parcelize
data class Recipe(
    @SerializedName("uri")
    val id: String, // uri
    @SerializedName("label")
    val title: String, // label
    val calories: Float, // calories
    val url: String, // url
    @SerializedName("image")
    val img: String, //image --> link a la imagen
    val ingredients: List<Ingredient>,
    @SerializedName("totalTime")
    val time: Int, //totalTime --> tiempo en minutos
) : Parcelable

data class Hit(
    val recipe: Recipe
)

data class EdamamRecipe(
    val hits: List<Hit>,
    val q: String
)

data class EdamamRecipeSearch(
    val recipes: List<Recipe>
)




