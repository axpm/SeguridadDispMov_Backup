package com.uc3m.searchyourrecipe.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.uc3m.searchyourrecipe.models.FavouriteRecipe
import com.uc3m.searchyourrecipe.database.FavouriteRecipeDatabase
import com.uc3m.searchyourrecipe.repository.FavouriteRecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteRecipeViewModel(application: Application): AndroidViewModel(application) {

    val readAll: LiveData<List<FavouriteRecipe>>

    private val repository: FavouriteRecipeRepository

    init {
        val favRecipeDAO = FavouriteRecipeDatabase.getDatabase(application).favRecipeDAO()
        repository = FavouriteRecipeRepository(favRecipeDAO)
        readAll = repository.readAll
    }

    fun addFavRecipe(favRecipe: FavouriteRecipe){
        viewModelScope.launch(Dispatchers.IO){
            repository.addFavRecipe(favRecipe)
        }
    }

    fun deleteFavRecipe(id: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteFavRecipeById(id)
        }
    }

    fun getFavRecipeById(id: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.getFavRecipeById(id)
        }
    }

    fun existsFavRecipeById(id: String): LiveData<Boolean>{
        val result = MutableLiveData<Boolean>()
        viewModelScope.launch(Dispatchers.IO){
            val ret = repository.existsFavRecipeById(id)
            result.postValue(ret)
        }
        return result
    }

    fun deleteAllFavRecipes(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllFavRecipes()
        }
    }


}