package com.uc3m.searchyourrecipe.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uc3m.searchyourrecipe.databinding.RecyclerViewIngredientItemBinding
import com.uc3m.searchyourrecipe.models.ShoppingListItem
import com.uc3m.searchyourrecipe.viewModels.ShoppingListItemViewModel

class ShoppingListAdapter(private val viewModel: ShoppingListItemViewModel) : RecyclerView.Adapter<ShoppingListAdapter.MyViewHolder>() {

    private var shoppingList = emptyList<ShoppingListItem>()
    class MyViewHolder(val binding: RecyclerViewIngredientItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecyclerViewIngredientItemBinding.inflate(LayoutInflater.from(parent.context), parent,
                false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = shoppingList[position]
        with(holder){
            binding.ingredient.text = currentItem.nameIngredient
            //Evento listener para eliminar un ingrediente
            binding.deleteIngredient.setOnClickListener{
                deleteIngre(position) //El metodo se encuentra abajo
            }
        }
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    fun setData(shoppingList: List<ShoppingListItem>){
        this.shoppingList = shoppingList
        //notificar cuando hay cambios y todos los que esten observando este liveData se actualizaran
        notifyDataSetChanged()
    }
    //Eiminamos el ingrediente de la bbdd
    fun deleteIngre(position: Int) {
        val item = shoppingList[position]
        //Cuando llamo al metodo, me indica que shoppingListItemViewModel no esta inicializado
        viewModel.deleteIngredient(item)
        notifyItemChanged(position)
    }


}