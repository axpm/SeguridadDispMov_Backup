package com.uc3m.searchyourrecipe.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.uc3m.searchyourrecipe.databinding.FragmentFavRecipesBinding
import com.uc3m.searchyourrecipe.viewModels.FavouriteRecipeViewModel

class FavRecipesFragment : Fragment() {
    private lateinit var  binding:  FragmentFavRecipesBinding
    private lateinit var favRecipesViewModel: FavouriteRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideKeyboard()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavRecipesBinding.inflate(inflater, container, false)
        val view = binding.root

        favRecipesViewModel = ViewModelProvider(this).get(FavouriteRecipeViewModel::class.java)

        val adapter = FavRecipesAdapter(requireContext(), favRecipesViewModel, viewLifecycleOwner)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        favRecipesViewModel.readAll.observe(viewLifecycleOwner, {
            favRecipe ->
            run {
                if (favRecipe.isNotEmpty()) {
                    adapter.setData(favRecipe)
                    binding.noRecipesFaved.visibility = View.GONE
                    binding.iconNoRecipesFaved.visibility = View.GONE
                }
                else {
                    adapter.setData(emptyList())
                    binding.noRecipesFaved.visibility = View.VISIBLE
                    binding.iconNoRecipesFaved.visibility = View.VISIBLE
                }

            }


        })


        return view
    }
}