package com.uc3m.searchyourrecipe.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.uc3m.searchyourrecipe.models.FavouriteRecipe

@Dao
interface FavouriteRecipeDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavRecipe(favRecipe: FavouriteRecipe)

    @Query("SELECT * FROM favorite_recipe_table ORDER BY title DESC")
    fun readAll(): LiveData<List<FavouriteRecipe>>

    @Query("DELETE FROM favorite_recipe_table WHERE id = :id")
    fun deleteFavRecipeById(id: String)

    @Query("SELECT * FROM favorite_recipe_table WHERE id = :id")
    fun getFavRecipeById(id: String): FavouriteRecipe

    @Query("SELECT EXISTS (SELECT 1 FROM favorite_recipe_table WHERE id = :id)")
    fun existsFavRecipeById(id: String): Boolean

    @Delete
    fun removeFavRecipe(favRecipe: FavouriteRecipe)

    //Eliminar todas las recetas favoritas
    @Query("DELETE FROM favorite_recipe_table")
    fun deleteAllFavRecipes()

}