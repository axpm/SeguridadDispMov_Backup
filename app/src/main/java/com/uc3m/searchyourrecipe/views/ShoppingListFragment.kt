package com.uc3m.searchyourrecipe.views

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.uc3m.searchyourrecipe.R
import com.uc3m.searchyourrecipe.databinding.FragmentShoppingListBinding
import com.uc3m.searchyourrecipe.databinding.RecyclerViewIngredientItemBinding
import com.uc3m.searchyourrecipe.viewModels.ShoppingListItemViewModel


class ShoppingListFragment : Fragment() {
    private lateinit var binding: FragmentShoppingListBinding
    private lateinit var bindingRecyclerView: RecyclerViewIngredientItemBinding
    private lateinit var shoppingListItemViewModel: ShoppingListItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).hideKeyboard()
        (activity as MainActivity).showBottomNavigation()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shopping_list_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_delete_all)

        searchItem.setOnMenuItemClickListener(object: MenuItem.OnMenuItemClickListener{
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                deleteAllItems()
                return false
            }
        })

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        bindingRecyclerView = RecyclerViewIngredientItemBinding.inflate(inflater, container, false)
        shoppingListItemViewModel = ViewModelProvider(this).get(ShoppingListItemViewModel::class.java)
        val view = binding.root

        //Lista de ingredientes
        val adapter = ShoppingListAdapter(shoppingListItemViewModel)
        val recyclerView = binding.recyclerViewShoppingList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        shoppingListItemViewModel = ViewModelProvider(this).get(ShoppingListItemViewModel::class.java)
        shoppingListItemViewModel.readAll.observe(viewLifecycleOwner, { shoppingListItem ->
            adapter.setData(shoppingListItem)
        })

        //Boton ADD
        binding.addIngredient.setOnClickListener{
            findNavController().navigate(R.id.action_shoppingListFragmentBis_to_new_ingredient)
        }

        return view
    }

    fun deleteAllItems(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Are you sure you want to delete all ingredients?")
        builder.setPositiveButton("Yes"){_,_ ->
            shoppingListItemViewModel.deleteAllIngredients()
            Toast.makeText(requireContext(),"All ingredients have been deleted",
                    Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_ -> }
        builder.create().show()
    }






}