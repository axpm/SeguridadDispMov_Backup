package com.uc3m.searchyourrecipe.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.uc3m.searchyourrecipe.R
import com.uc3m.searchyourrecipe.viewModels.UserViewModel

class SplashActivity : AppCompatActivity() {

    // This is the loading time of the splash screen
    private val SPLASH_TIME_OUT:Long = 1500 // 1.5 sec

    private val TEST: Boolean = false
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        userViewModel.readAll.observe(this, { list ->
            run {
                if(list.isEmpty()){
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, LogInActivity::class.java))

                        // close this activity
                        finish()
                    }, SPLASH_TIME_OUT)
                }else{
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, MainActivity::class.java))

                        // close this activity
                        finish()
                    }, SPLASH_TIME_OUT)
                }
            }

        })
    }
}