package com.uc3m.searchyourrecipe.views

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.uc3m.searchyourrecipe.R
import com.uc3m.searchyourrecipe.databinding.RecyclerViewRecipeItemBinding
import com.uc3m.searchyourrecipe.models.FavouriteRecipe
import com.uc3m.searchyourrecipe.viewModels.FavouriteRecipeViewModel


class FavRecipesAdapter(private val context: Context, private val viewModel: FavouriteRecipeViewModel, private val viewLifecycleOwner: LifecycleOwner): RecyclerView.Adapter<FavRecipesAdapter.MyViewHolder>() {

    private var favRecipesList = emptyList<FavouriteRecipe>()

    class MyViewHolder(val binding: RecyclerViewRecipeItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecyclerViewRecipeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = favRecipesList[position]
        with(holder){
            binding.recipeName.text = currentItem.title.toString()
            binding.time.text = currentItem.time.toString() + "min"
            Picasso.get().load(currentItem.img).into(binding.imageRecipe)

            //Si pinchamos sobre la imagen o el nombre, accedemos a la informacion de la receta
            binding.imageRecipe.setOnClickListener {
                val action = FavRecipesFragmentDirections
                    .actionFavRecipesFragmentToRecipeFragment(currentItem.id.toString(), "", null)
                holder.itemView.findNavController().navigate(action)
            }

            binding.recipeName.setOnClickListener {
                val action = FavRecipesFragmentDirections
                    .actionFavRecipesFragmentToRecipeFragment(currentItem.id.toString(), "", null)
                holder.itemView.findNavController().navigate(action)
            }
            //Listar las recetas
            isInFav(currentItem, binding)

        }
    }

    override fun getItemCount(): Int {
        return favRecipesList.size
    }

    fun setData(list: List<FavouriteRecipe>){
        this.favRecipesList = list
        notifyDataSetChanged()
    }

    private fun addFavRecipe(newFav: FavouriteRecipe) {
        viewModel.addFavRecipe(newFav)
    }

    private fun removeFavRecipe(id: String) {
        viewModel.deleteFavRecipe(id)
        //notifyItemChanged(position)
    }

    private fun isInFav(currentItem: FavouriteRecipe, binding: RecyclerViewRecipeItemBinding) {

        viewModel.existsFavRecipeById(currentItem.id).observe(viewLifecycleOwner, {
            ret ->
            run {
                // comprobar el id para cambiar
                if ( ret ){
                    // poner estrella rellena
                    binding.starButton.setImageResource(R.drawable.ic_baseline_star_24)
                    // eliminar de favoritos y cambiar el icono
                    binding.starButton.setOnClickListener{
                        val builder = AlertDialog.Builder(context)
                        builder.setTitle("Are you sure you want to remove from favorite?")
                        builder.setPositiveButton("Yes"){_,_ ->
                            removeFavRecipe(currentItem.id)
                            Toast.makeText(context,"Recipe has been remove from favorite",
                                    Toast.LENGTH_SHORT).show()
                        }
                        builder.setNegativeButton("No"){_,_ -> }
                        builder.create().show()

                    }
                }
            }

        })

    }

}