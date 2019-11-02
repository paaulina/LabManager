package com.example.labmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.util.Log


class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        InternetConnectionChecker.checkConnection(this@LoginActivity, this)
    }


    fun logUser(view: View){
        var emailText = edit_text_email.text.toString()
        var passwordText = edit_text_password.text.toString()
        if(correctEmailValue(emailText) && correctPasswordValue(passwordText) && InternetConnectionChecker.checkConnection(this@LoginActivity, this)){

            mAuth?.signInWithEmailAndPassword(emailText, passwordText)!!
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth?.getCurrentUser()
                        if (user != null) {
                            goToMainPageActivity(user)
                        }
                    } else {
                        Toast.makeText(
                            this@LoginActivity, "Zły login lub hasło.",
                            Toast.LENGTH_SHORT
                        ).show()
                        setEmailError()
                        setPasswordError()

                    }
                }
        }
    }

    fun correctEmailValue(emailText: String): Boolean {
        if(!TextValidation.correctEmailValue(emailText)){
           setEmailError()
            return false
        }
        return true
    }

    fun correctPasswordValue(passwordText: String) : Boolean{
        if(!passwordText.trim().isNotEmpty()){
            setPasswordError()
            return false
        }
        return true
    }

    fun setEmailError(){
        layout_email.isErrorEnabled = true
        layout_email.error = TextValidation.EMAIL_ERROR_TEXT
    }

    fun setPasswordError(){
        layout_password.isErrorEnabled = true
        layout_password.error = TextValidation.EMAIL_ERROR_TEXT
    }


    fun goToMainPageActivity(user: FirebaseUser){
        val intent = Intent(this, MainPageActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    fun goToSignupActivity(view: View){
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    fun goToResetPasswordActivity(view: View){
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }



}
