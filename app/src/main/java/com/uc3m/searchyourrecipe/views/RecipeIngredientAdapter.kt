package com.uc3m.searchyourrecipe.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uc3m.searchyourrecipe.databinding.RecyclerViewIngredientRecipeItemBinding
import com.uc3m.searchyourrecipe.models.Ingredient

class RecipeIngredientAdapter: RecyclerView.Adapter<RecipeIngredientAdapter.MyViewHolder>() {

    private var recipesList = emptyList<Ingredient>()

    class MyViewHolder(val binding: RecyclerViewIngredientRecipeItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecyclerViewIngredientRecipeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = recipesList[position]
        with(holder){

            binding.ingredient.text = currentItem.text
        }
    }

    override fun getItemCount(): Int {
        return recipesList.size
    }

    fun setData(list: List<Ingredient>){
        this.recipesList = list
        notifyDataSetChanged()
    }

}