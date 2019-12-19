package com.example.labmanager.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.labmanager.R
import com.example.labmanager.VERIFY_EMAIL_ERROR_CODE
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_confirmation.*

class ConfirmationActivity : AppCompatActivity() {

    var message = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        message = intent.getStringExtra("message")
        if(message.equals(VERIFY_EMAIL_ERROR_CODE)){
            textViewMessage.text = "Proszę potwiedź konto poprzez link wysłany na podany adres e-mail."
            button_send.visibility = View.VISIBLE
        } else{
            textViewMessage.text = message
        }

    }

    override fun onBackPressed() {}

    fun goBackToStartActivity(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun sendEmailAgain(view: View){
        button_send.visibility = View.GONE
        var mAuth = FirebaseAuth.getInstance()
        if(mAuth != null){
            val user = mAuth!!.getCurrentUser()
            user!!.sendEmailVerification().addOnCompleteListener{
                textViewMessage.text = "Email został wysłany ponownie."
            }
        }
    }
}
