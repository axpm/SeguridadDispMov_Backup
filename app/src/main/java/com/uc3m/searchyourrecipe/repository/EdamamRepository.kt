package com.uc3m.searchyourrecipe.repository

import com.uc3m.searchyourrecipe.api.RetroFitInstance
import com.uc3m.searchyourrecipe.models.EdamamRecipe
import com.uc3m.searchyourrecipe.models.Recipe
import retrofit2.Response

class EdamamRepository {

    suspend fun searchRecipe(query: String, app_id: String, app_key: String): Response<EdamamRecipe>{
        return RetroFitInstance.edamamAPI.searchRecipe(query, app_id, app_key)
    }

    suspend fun getRecipe(uri: String, app_id: String, app_key: String): Response<List<Recipe>>{
        return RetroFitInstance.edamamAPI.getRecipe(uri, app_id, app_key)
    }

}