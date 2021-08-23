package com.uc3m.searchyourrecipe.views

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.uc3m.searchyourrecipe.BuildConfig
import com.uc3m.searchyourrecipe.R
import com.uc3m.searchyourrecipe.databinding.FragmentSearchBinding
import com.uc3m.searchyourrecipe.models.Hit
import com.uc3m.searchyourrecipe.repository.EdamamRepository
import com.uc3m.searchyourrecipe.viewModels.FavouriteRecipeViewModel
import com.uc3m.searchyourrecipe.viewModels.SearchViewModel
import com.uc3m.searchyourrecipe.viewModels.SearchViewModelFactory

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var favRecipesViewModel: FavouriteRecipeViewModel

    private var recipeListData: List<Hit> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)

        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if (query.length > 128){
                        Toast.makeText(requireContext(),"Too long!! Try to be more specific", Toast.LENGTH_SHORT).show()
                    }else{
                        executeQuery(query)
                        binding.noRecipesSearched.visibility = View.GONE
                        binding.iconNoRecipesSearched.visibility = View.GONE
                    }
                }


                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root


        if (recipeListData.isNotEmpty()) {

            val adapter = SearchAdapter(favRecipesViewModel, viewLifecycleOwner)
            val recyclerView = binding.recyclerView
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext())

            adapter.setData(recipeListData)

            binding.noRecipesSearched.visibility = View.GONE
            binding.iconNoRecipesSearched.visibility = View.GONE

        }
        else {

            binding.noRecipesSearched.visibility = View.VISIBLE
            binding.iconNoRecipesSearched.visibility = View.VISIBLE
        }

        return view
    }


    private fun executeQuery(query: String?) {
        // FAV RECIPES
        favRecipesViewModel = ViewModelProvider(this).get(FavouriteRecipeViewModel::class.java)

        // SEARCH
        val edamamRepository = EdamamRepository()

        val searchViewModelFactory = SearchViewModelFactory(edamamRepository)
        val searchViewModel = ViewModelProvider(this, searchViewModelFactory).get(SearchViewModel::class.java)

        if (query != null) {

            val app_id: String = BuildConfig.API_ID
            val app_key: String = BuildConfig.API_KEY

            searchViewModel.searchRecipe(query, app_id, app_key)
        }

        val adapter = SearchAdapter(favRecipesViewModel, viewLifecycleOwner)
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchViewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful){

                val hits = response.body()?.hits

                if (hits != null) {
                    if (hits.isNotEmpty()) {
                        recipeListData = hits

                        adapter.setData(hits)

                    }else {
                        //Actualizar el recyclerView cuando la busqueda no devuelve ninguna receta
                        adapter.setData(emptyList())
                        Toast.makeText(requireContext(),query + " not found",Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                //Log.d("Response", response.errorBody().toString())

                Toast.makeText(requireContext(),"Something went wrong",
                        Toast.LENGTH_SHORT).show()

            }
        })

    }





}

