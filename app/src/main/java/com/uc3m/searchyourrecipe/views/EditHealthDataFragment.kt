package com.uc3m.searchyourrecipe.views

import android.os.Bundle
import android.text.Editable
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.uc3m.searchyourrecipe.R
import com.uc3m.searchyourrecipe.databinding.FragmentEditHealthDataBinding
import com.uc3m.searchyourrecipe.models.User
import com.uc3m.searchyourrecipe.viewModels.UserViewModel

class EditHealthDataFragment : Fragment() {

    private lateinit var binding: FragmentEditHealthDataBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var userViewModel: UserViewModel

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditHealthDataBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.saveButton.setOnClickListener {
            val age = binding.ageInput.text
            val height = binding.heightInput.text
            val weight = binding.weightInput.text
            lateinit var modUser: User

                    //Comprobamos que los valores esten en el formato correcto
            if(checkValues(age, height, weight)){
                //saveHealthData(age, height, weight)
                auth = FirebaseAuth.getInstance()
                userViewModel.getUser(auth.currentUser.email).observe(viewLifecycleOwner, {
                    user ->
                    run {

                        val eAge = userViewModel.encryptData(age.toString())
                        val encodedIVAge: String = Base64.encodeToString(eAge.first, Base64.DEFAULT)
                        val encodedAge: String = Base64.encodeToString(eAge.second, Base64.DEFAULT)

                        val eHeight = userViewModel.encryptData(height.toString())
                        val encodedIVHeight: String = Base64.encodeToString(eHeight.first, Base64.DEFAULT)
                        val encodedHeight: String = Base64.encodeToString(eHeight.second, Base64.DEFAULT)

                        val eWeight = userViewModel.encryptData(weight.toString())
                        val encodedIVWeight: String = Base64.encodeToString(eWeight.first, Base64.DEFAULT)
                        val encodedWeight: String = Base64.encodeToString(eWeight.second, Base64.DEFAULT)

                        modUser = User( user.email, user.name, user.image, user.ivImage,
                                            encodedAge, encodedIVAge, encodedHeight, encodedIVHeight,
                                            encodedWeight, encodedIVWeight)

                        userViewModel.modifyUser(modUser)

                        findNavController().navigate(R.id.action_editHealthDataFragment_to_healthDataFragment)

                    }
                })
            }
        }


        return view
    }

    private fun checkValues(age: Editable?, height: Editable?, weight: Editable?): Boolean {
        var error: Boolean = false
        if (age != null && height != null && weight != null && age.isNotEmpty() && height.isNotEmpty() && weight.isNotEmpty()) {
            if (!(Integer.parseInt(age.toString()) in 1..129)) {
                Toast.makeText(requireContext(), "Insert a correct age", Toast.LENGTH_LONG).show()
                error = true
            }
            if (!(weight.toString().toFloat() > 0.0 && weight.toString().toFloat() < 400.0)) {
                Toast.makeText(requireContext(), "Insert a correct weight", Toast.LENGTH_LONG).show()
                error = true
            }
            if (!(height.toString().toFloat() > 0.0 && height.toString().toFloat() < 3.0)) {
                Toast.makeText(requireContext(), "Insert a correct height", Toast.LENGTH_LONG).show()
                error = true
            }
            return !error
        }
        Toast.makeText(requireContext(), "Fill all the fields", Toast.LENGTH_LONG).show()

        return false
    }

}