package com.example.labmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.edit_text_email
import kotlinx.android.synthetic.main.activity_reset_password.*
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener



class ResetPasswordActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        mAuth = FirebaseAuth.getInstance()
    }

    fun resetPassword(view : View){
        var emailText = edit_text_email.text.toString()
        if(!TextValidation.correctEmailValue(emailText)){
            layout_email_text.isErrorEnabled = true
            layout_email_text.error = TextValidation.EMAIL_ERROR_TEXT
            return
        }
        layout_email_text.isErrorEnabled = false
        mAuth?.sendPasswordResetEmail(emailText)
             ?.addOnCompleteListener(OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    passwordResetSuccess(emailText)
                } else {
                    Log.d("ERROR", "wrong email")
                }
            })

    }

    fun passwordResetSuccess(emailText: String){
        val intent = Intent(this, ConfirmationActivity::class.java)
        intent.putExtra("message", "Link resetujący hasło został wysłany na adres: $emailText")
        startActivity(intent)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}