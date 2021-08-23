package com.uc3m.searchyourrecipe.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.uc3m.searchyourrecipe.models.FavouriteRecipe

@Database(entities = [FavouriteRecipe::class], version = 1, exportSchema = false)
abstract class FavouriteRecipeDatabase: RoomDatabase() {

    abstract fun favRecipeDAO(): FavouriteRecipeDAO

    companion object{
        @Volatile
        private var INSTANCE: FavouriteRecipeDatabase? = null

        fun getDatabase(context: Context): FavouriteRecipeDatabase {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavouriteRecipeDatabase::class.java,
                        "favorite_recipe_database"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }

    }
}