package com.uc3m.searchyourrecipe.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.uc3m.searchyourrecipe.BuildConfig
import com.uc3m.searchyourrecipe.databinding.FragmentRecipeBinding
import com.uc3m.searchyourrecipe.models.Ingredient
import com.uc3m.searchyourrecipe.models.ShoppingListItem
import com.uc3m.searchyourrecipe.repository.EdamamRepository
import com.uc3m.searchyourrecipe.viewModels.SearchViewModel
import com.uc3m.searchyourrecipe.viewModels.SearchViewModelFactory
import com.uc3m.searchyourrecipe.viewModels.ShoppingListItemViewModel

class RecipeFragment : Fragment() {

    val args: RecipeFragmentArgs by navArgs()
    private lateinit var binding: FragmentRecipeBinding
    private lateinit var shoppingListItemViewModel: ShoppingListItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideKeyboard()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecipeBinding.inflate(inflater, container, false)

        shoppingListItemViewModel = ViewModelProvider(this).get(ShoppingListItemViewModel::class.java)


        val view = binding.root

        // recogemos el parámetro "id"
        val uriRecipe = args.id
        // cambiar el título
        val title = binding.title
        title.text = " "
        val adapter = RecipeIngredientAdapter()
        val recyclerView = binding.ingredientsList
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        // SEARCH
        if(args.source == "search"){
            val recipe = args.recipe
            if (recipe != null) {
                binding.title.text = recipe.title
                Picasso.get().load(recipe.img).into(binding.imageView)
                binding.webRecipe.setOnClickListener{
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse(recipe.url)
                    startActivity(openURL)
                }
                adapter.setData(recipe.ingredients)
                binding.ingredientsButton.setOnClickListener{
                    insertDataToDatabase(recipe.ingredients)
                }
            }
        }else{

            val edamamRepository = EdamamRepository()
            val searchViewModelFactory = SearchViewModelFactory(edamamRepository)
            val searchViewModel = ViewModelProvider(this, searchViewModelFactory).get(
                SearchViewModel::class.java
            )

            val app_id: String = BuildConfig.API_ID
            val app_key: String = BuildConfig.API_KEY

            searchViewModel.getRecipe(uriRecipe, app_id, app_key)

            searchViewModel.getResponse.observe(viewLifecycleOwner, Observer { response ->
                if (response.isSuccessful) {
                    val recipe = response.body()?.get(0)
                    // Rellenar información
                    if (recipe != null) {
                        binding.title.text = recipe.title
                        Picasso.get().load(recipe.img).into(binding.imageView)
                        binding.webRecipe.setOnClickListener {
                            val openURL = Intent(Intent.ACTION_VIEW)
                            openURL.data = Uri.parse(recipe.url)
                            startActivity(openURL)
                        }
                        adapter.setData(recipe.ingredients)
                        binding.ingredientsButton.setOnClickListener {
                            insertDataToDatabase(recipe.ingredients)
                        }
                    }

                    //Log.d("Response", recipeTitle.toString())

                } else {
                    Log.d("Response", response.errorBody().toString())

                    Toast.makeText(
                        requireContext(), "Something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()

                    title.text = "Error"

                }
            })
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDetach() {
        (activity as MainActivity).showBottomNavigation()
        super.onDetach()
    }


    //Comunicarse con Room
    private fun insertDataToDatabase(list: List<Ingredient>) {
        for (item in list) {
            //validamos
            val ingredientName = item.text
            if (inputCheck(ingredientName)) {
                val ingredient = ShoppingListItem(ingredientName)
                shoppingListItemViewModel.addIngredient(ingredient)
            }
        }
            Toast.makeText(requireContext(), "Ingredients Added", Toast.LENGTH_LONG).show()
        }

    //Validar que no esten vacios
    private fun inputCheck(ingredientName: String): Boolean{
        //Toast.makeText(requireContext(), "There are no ingredients to add", Toast.LENGTH_LONG).show()
        return !(TextUtils.isEmpty(ingredientName))
    }


}