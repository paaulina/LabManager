package com.example.labmanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.labmanager.R
import com.example.labmanager.VERIFY_EMAIL_ERROR_CODE
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_confirmation.*

class ConfirmationActivity : AppCompatActivity() {

    private var message = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)
        message = intent.getStringExtra("message")
        if(message == VERIFY_EMAIL_ERROR_CODE){
            textViewMessage.text = getString(R.string.confirm_your_email)
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
        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener{
            textViewMessage.text = getString(R.string.email_has_been_successfully_sent)
        }
    }
}
