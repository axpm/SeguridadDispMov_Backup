package com.uc3m.searchyourrecipe.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uc3m.searchyourrecipe.R
import com.uc3m.searchyourrecipe.databinding.RecyclerViewRecipeItemBinding
import com.uc3m.searchyourrecipe.models.FavouriteRecipe
import com.uc3m.searchyourrecipe.models.Hit
import com.uc3m.searchyourrecipe.models.Recipe
import com.uc3m.searchyourrecipe.viewModels.FavouriteRecipeViewModel

class SearchAdapter(private val favViewModel: FavouriteRecipeViewModel, private val viewLifecycleOwner: LifecycleOwner): RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    private var recipesList = emptyList<Hit>()

    class MyViewHolder(val binding: RecyclerViewRecipeItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecyclerViewRecipeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = recipesList[position].recipe
        with(holder){
            binding.recipeName.text = currentItem.title
            binding.time.text = currentItem.time.toString() + "min"

            Picasso.get().load(currentItem.img).into(binding.imageRecipe)

            binding.imageRecipe.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToRecipeFragment(currentItem.id, "search", currentItem)
                holder.itemView.findNavController().navigate(action)
            }
            binding.recipeName.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToRecipeFragment(currentItem.id, "search", currentItem)
                holder.itemView.findNavController().navigate(action)
            }
            isInFav(currentItem, position, binding)

        }
    }

    private fun addFavRecipe(newFav: FavouriteRecipe, position: Int) {
        favViewModel.addFavRecipe(newFav)
        notifyItemChanged(position)
    }

    private fun removeFavRecipe(id: String, position: Int) {
        favViewModel.deleteFavRecipe(id)
        notifyItemChanged(position)
    }

    private fun isInFav(currentItem: Recipe, position: Int, binding: RecyclerViewRecipeItemBinding) {

        favViewModel.existsFavRecipeById(currentItem.id).observe(viewLifecycleOwner, {
            ret ->
            run {
                // comprobar si esta en favoritos para cambiar la apariecia
                if ( ret ){
                    // poner estrella rellena
                    binding.starButton.setImageResource(R.drawable.ic_baseline_star_24)
                    // eliminar de favoritos y cambiar el icono
                    binding.starButton.setOnClickListener{
                        removeFavRecipe(currentItem.id, position)
                        binding.starButton.setImageResource(R.drawable.ic_baseline_star_border_24)
                    }
                }else{
                    // poner estrella sin relleno
                    binding.starButton.setImageResource(R.drawable.ic_baseline_star_border_24)
                    // añadir a favoritos y cambiar el icono
                    binding.starButton.setOnClickListener{
                        val newFav = FavouriteRecipe(currentItem.id, currentItem.title, currentItem.img, currentItem.time)
                        addFavRecipe(newFav, position)
                        binding.starButton.setImageResource(R.drawable.ic_baseline_star_24)
                    }
                }
            }

        })

    }

    override fun getItemCount(): Int {
        return recipesList.size
    }

    fun setData(list: List<Hit>){
        this.recipesList = list
        notifyDataSetChanged()
    }

}