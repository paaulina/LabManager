package com.example.labmanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.labmanager.R
import com.example.labmanager.service.InternetConnectionChecker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class MainActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN)
        mAuth = FirebaseAuth.getInstance()
        setContentView(R.layout.activity_main)

        InternetConnectionChecker.checkConnection(
            this,
            this,
            resources.getString(R.string.app_cant_work_incorrectly_alert)
        )
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = mAuth?.currentUser
        updateUI(currentUser)
    }

    override fun onBackPressed() {}

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null && currentUser.isEmailVerified){
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }
    }


    fun goToLoginActivity(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun goToSignupActivity(view: View){
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }
}
