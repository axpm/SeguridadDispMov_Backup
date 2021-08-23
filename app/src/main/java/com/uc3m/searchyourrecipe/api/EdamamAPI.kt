package com.uc3m.searchyourrecipe.api

import com.uc3m.searchyourrecipe.models.EdamamRecipe
import com.uc3m.searchyourrecipe.models.Recipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EdamamAPI {

    @GET("/search")
    suspend fun searchRecipe(@Query("q") recipe: String,
                             @Query("app_id") appID: String,
                             @Query("app_key") appKey: String,
                             @Query("from") from: Int = 0,
                             @Query("to") to: Int = 15,
    ): Response<EdamamRecipe>;

    @GET("/search")
    suspend fun getRecipe(@Query("r") id: String,
                          @Query("app_id") appID: String,
                          @Query("app_key") appKey: String
    ): Response<List<Recipe>>;

}