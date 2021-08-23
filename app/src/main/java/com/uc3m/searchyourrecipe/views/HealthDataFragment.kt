package com.uc3m.searchyourrecipe.views

import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.google.firebase.auth.FirebaseAuth
import com.uc3m.searchyourrecipe.R
import com.uc3m.searchyourrecipe.databinding.FragmentHealthDataBinding
import com.uc3m.searchyourrecipe.viewModels.UserViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import javax.crypto.KeyGenerator

class HealthDataFragment : Fragment() {

    private lateinit var binding: FragmentHealthDataBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        if (!userViewModel.checkKey()){
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec
                    .Builder("MyKeyStore", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHealthDataBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.editButton.setOnClickListener {
            findNavController().navigate(R.id.action_healthDataFragment_to_editHealthDataFragment)
        }

        //readHealthData()

        auth = FirebaseAuth.getInstance()
        userViewModel.getUser(auth.currentUser.email).observe(viewLifecycleOwner, {
            user ->
            run {

                if(user.ivAge == null || user.ivHeight == null || user.ivWeight == null){
                    binding.ageText.text = "Age"
                    binding.heightText.text = "Height"
                    binding.weightText.text = "Weight"
                    binding.imcText.text = "IMC"

                }else{
                    val ivAge: ByteArray = Base64.decode(user.ivAge , Base64.DEFAULT)
                    val eAge: ByteArray = Base64.decode(user.age, Base64.DEFAULT)
                    binding.ageText.text = userViewModel.decryptData(ivAge, eAge)

                    val ivHeight: ByteArray = Base64.decode(user.ivHeight, Base64.DEFAULT)
                    val eHeight: ByteArray = Base64.decode(user.height, Base64.DEFAULT)
                    val height = userViewModel.decryptData(ivHeight, eHeight)
                    binding.heightText.text = height

                    val ivWeight: ByteArray = Base64.decode(user.ivWeight, Base64.DEFAULT)
                    val eWeight: ByteArray = Base64.decode(user.weight, Base64.DEFAULT)
                    val weight = userViewModel.decryptData(ivWeight, eWeight)
                    binding.weightText.text = weight
                    binding.imcText.text = calculateIMC(height, weight)
                }

            }
        })

        return view
    }

    private fun readHealthData() {
        val mainKey = MasterKey.Builder(requireContext())
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        val fileToRead = "health_data.txt"
        val DIRECTORY = context?.filesDir?.path
        if (File(DIRECTORY, fileToRead).exists()){
            val encryptedFile = EncryptedFile.Builder(
                    requireContext(),
                    File(DIRECTORY, fileToRead),
                    mainKey,
                    EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
            val inputStream = encryptedFile.openFileInput()
            val byteArrayOutputStream = ByteArrayOutputStream()
            var nextByte: Int = inputStream.read()
            while (nextByte != -1) {
                byteArrayOutputStream.write(nextByte)
                nextByte = inputStream.read()
            }
            val plaintext: ByteArray = byteArrayOutputStream.toByteArray()
            val plainString = String(plaintext)

            if (plainString != ""){
                val strs = plainString.split("||").toTypedArray()
                val height = strs[1].split(":").toTypedArray()[1]
                val weight = strs[2].split(":").toTypedArray()[1]
                binding.ageText.text = strs[0].split(":").toTypedArray()[1].toString()
                binding.heightText.text = height.toString()
                binding.weightText.text = weight.toString()
                binding.imcText.text = calculateIMC(height, weight)
            }
        }


    }

    private fun calculateIMC(height: String, weight: String): CharSequence? {
        if (height == null || height == "height" || weight == null || height == "height") return "0"
        return (weight.toFloat() / (height.toFloat() * height.toFloat())).toString()
    }

}