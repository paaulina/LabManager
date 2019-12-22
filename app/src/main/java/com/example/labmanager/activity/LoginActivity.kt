package com.example.labmanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.example.labmanager.*
import com.google.firebase.auth.FirebaseUser
import com.example.labmanager.service.InternetConnectionChecker
import com.example.labmanager.service.TextValidation


class LoginActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        InternetConnectionChecker.checkConnection(
            this@LoginActivity,
            this,
            resources.getString(R.string.app_cant_work_incorrectly_alert)
        )
    }


    fun logUser(view: View){
        val emailText = edit_text_email.text.toString()
        val passwordText = edit_text_password.text.toString()
        if(correctEmailValue(emailText) && correctPasswordValue(passwordText)
            && InternetConnectionChecker.checkConnection(
                this@LoginActivity,
                this,
                resources.getString(R.string.set_up_connection_to_continue)
            )
        ){

            mAuth?.signInWithEmailAndPassword(emailText, passwordText)!!
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth?.currentUser
                        if (user != null) {

                            if(user.isEmailVerified){
                                goToMainPageActivity(user)
                            }else{
                                val intent = Intent(this, ConfirmationActivity::class.java)
                                intent.putExtra("message", VERIFY_EMAIL_ERROR_CODE)
                                startActivity(intent)
                            }

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

    private fun correctEmailValue(emailText: String): Boolean {
        if(!TextValidation.correctEmailValue(emailText)){
           setEmailError()
            return false
        }
        return true
    }

    private fun correctPasswordValue(passwordText: String) : Boolean{
        if(passwordText.trim().isEmpty()){
            setPasswordError()
            return false
        }
        return true
    }

    private fun setEmailError(){
        layout_email.isErrorEnabled = true
        layout_email.error = EMAIL_ERROR_TEXT
    }

    private fun setPasswordError(){
        layout_password.isErrorEnabled = true
        layout_password.error = EMAIL_ERROR_TEXT
    }


    private fun goToMainPageActivity(user: FirebaseUser){
        val intent = Intent(this, MainPageActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

    private fun goToSignupActivity(view: View){
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun goToResetPasswordActivity(view: View){
        val intent = Intent(this, ResetPasswordActivity::class.java)
        intent.putExtra(PASSWOED_ENTRY_POINT, RESET_PASSWORD_ENTRY)
        startActivity(intent)
    }



}
