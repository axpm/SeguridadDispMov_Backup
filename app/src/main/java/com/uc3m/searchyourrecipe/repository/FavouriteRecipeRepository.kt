package com.uc3m.searchyourrecipe.repository

import androidx.lifecycle.LiveData
import com.uc3m.searchyourrecipe.models.FavouriteRecipe
import com.uc3m.searchyourrecipe.database.FavouriteRecipeDAO

class FavouriteRecipeRepository(private val favRecipeDAO: FavouriteRecipeDAO) {

    val readAll: LiveData<List<FavouriteRecipe>> = favRecipeDAO.readAll()

    suspend fun addFavRecipe(favRecipe: FavouriteRecipe){
        favRecipeDAO.addFavRecipe(favRecipe)
    }

    fun deleteFavRecipeById(id: String){
        favRecipeDAO.deleteFavRecipeById(id)
    }

    fun getFavRecipeById(id: String): FavouriteRecipe {
        return favRecipeDAO.getFavRecipeById(id)
    }

    fun existsFavRecipeById(id: String): Boolean {
        return favRecipeDAO.existsFavRecipeById(id)
    }

    fun deleteAllFavRecipes(){
        return favRecipeDAO.deleteAllFavRecipes()
    }
}