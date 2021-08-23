package com.uc3m.searchyourrecipe.views

import android.content.Intent
import androidx.biometric.BiometricPrompt;
import android.os.Bundle
import android.util.Base64
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import com.uc3m.searchyourrecipe.R
import com.uc3m.searchyourrecipe.databinding.FragmentUserBinding
import com.uc3m.searchyourrecipe.viewModels.FavouriteRecipeViewModel
import com.uc3m.searchyourrecipe.viewModels.ShoppingListItemViewModel
import com.uc3m.searchyourrecipe.viewModels.UserViewModel
import java.util.concurrent.Executor
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var auth : FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var userViewModel: UserViewModel
    private lateinit var favouriteRecipeViewModel: FavouriteRecipeViewModel
    private lateinit var shoppingListItemViewModel: ShoppingListItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        favouriteRecipeViewModel = ViewModelProvider(this).get(FavouriteRecipeViewModel::class.java)
        shoppingListItemViewModel = ViewModelProvider(this).get(ShoppingListItemViewModel::class.java)

        setHasOptionsMenu(true)
        (activity as MainActivity).hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_menu, menu)

        val logout: MenuItem = menu.findItem(R.id.action_logout)

        logout.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                auth.signOut()
                googleSignInClient.signOut()
                //LoginManager.getInstance().logOut()

                //Navegar al loginActivity
                startActivity(Intent(activity, LogInActivity::class.java))
                // close the main activity
                activity?.finish()

                // Eliminar al usuario de la bbdd
                userViewModel.deleteAllUsers()
                // Eliminar todos los favoritos
                favouriteRecipeViewModel.deleteAllFavRecipes()
                // Eliminar todos los ingredientes
                shoppingListItemViewModel.deleteAllIngredients()
                return false
            }
        })
    }

    //Al tener distintos activity, creamos esta funcion para evitar que un atacante salte el login
    private fun checkUserLogged() {
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.readAll.observe(viewLifecycleOwner, { list ->
            run {
                if (list.isEmpty()) {
                    startActivity(Intent(requireContext(), LogInActivity::class.java))

                    // close this activity
                    (activity as MainActivity).finish()
                } else {
                    binding.privateButton.setOnClickListener {
                        configureBiometric()
                        biometricPrompt.authenticate(promptInfo)
                    }

                    showDataUser(binding)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root

        // Configure Google Sign In inside onCreate method
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        auth = FirebaseAuth.getInstance()

        //Comprobamos que el usuario este loggeado por seguridad
        checkUserLogged()

        return view
    }

    private fun showDataUser(binding: FragmentUserBinding) {
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.getUser(auth.currentUser.email).observe(viewLifecycleOwner, { user ->
            run {
                val ivImage: ByteArray = Base64.decode(user.ivImage, Base64.DEFAULT)
                val eImage: ByteArray = Base64.decode(user.image, Base64.DEFAULT)
                Picasso.get().load(userViewModel.decryptData(ivImage, eImage))
                    .into(binding.imageUser)

                binding.userName.text = user.name

            }
        })
    }

    private fun configureBiometric() {
        // se usa setDeviceCredentialAllowed aunque está deprecated porque es lo que funciona en
        // Android 10 y 11 y podemos probarlo en el móvil físico y virtual
        promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login")
                .setSubtitle("We care about your privacy")
                .setDeviceCredentialAllowed(true)
                .build()

        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext(),
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(
                        requireContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT
                    )
                        .show()

                    findNavController().navigate(R.id.action_userFragment_to_healthDataFragment)

                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        requireContext(), "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })

    }

}