package com.uc3m.searchyourrecipe.views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.uc3m.searchyourrecipe.R


class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    private fun setupViews(){
        // Coger el BottomNavigationView
        bottomNavView = findViewById(R.id.bottomNavView)

        // Coger el Navigation Controller
        val navController = findNavController(R.id.fragNavHost)

        // Asociar el la Navigation Controller con el BottomNavigationView
        bottomNavView.setupWithNavController(navController)

        // Setting Up ActionBar with Navigation Controller
        // Pass the IDs of top-level destinations in AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(
                topLevelDestinationIds = setOf(
                        R.id.userFragment,
                        R.id.searchFragment,
                        R.id.favRecipesFragment,
                        R.id.shoppingListFragmentBis,

                )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.fragNavHost).navigateUp()

    //Mostrar u ocultar el menu de navegacion
    fun showBottomNavigation(){
        bottomNavView.visibility = View.VISIBLE
    }

    fun hideBottomNavigation(){
        bottomNavView.visibility = View.GONE
    }

    @SuppressLint("ServiceCast")
    fun hideKeyboard() {
        val inputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // Check if no view has focus
        val currentFocusedView = this.currentFocus
        currentFocusedView?.let {
            inputMethodManager.hideSoftInputFromWindow(
                    currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}