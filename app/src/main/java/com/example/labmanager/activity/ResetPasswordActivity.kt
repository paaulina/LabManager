package com.example.labmanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.labmanager.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.edit_text_email
import kotlinx.android.synthetic.main.activity_reset_password.*
import com.example.labmanager.service.InternetConnectionChecker
import com.example.labmanager.service.TextValidation
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.android.synthetic.main.activity_confirmation.*


class ResetPasswordActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var message = ""
    private var checkEmail = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        message = intent.getStringExtra(PASSWOED_ENTRY_POINT)
        if(message == CHANGE_PASSWORD_ENTRY){
            tv_message.text = getString(R.string.ineert_email_to_continue)
            button_reset.setText(getString(R.string.send_reset_link))
            checkEmail = true
        }

        mAuth = FirebaseAuth.getInstance()
        InternetConnectionChecker.checkConnection(
            this,
            this,
            resources.getString(com.example.labmanager.R.string.app_cant_work_incorrectly_alert)
        )
    }

    fun resetPassword(view : View){

        var emailText = edit_text_email.text.toString()
        if(!TextValidation.correctEmailValue(emailText)){
            layout_email_text.isErrorEnabled = true
            layout_email_text.error = EMAIL_ERROR_TEXT
            return
        }

        if(checkEmail){
            var user = FirebaseAuth.getInstance().currentUser
            if(user == null || !user.email.equals(emailText)){
                layout_email_text.isErrorEnabled = true
                layout_email_text.error = EMAIL_ERROR_TEXT_WRONG
                return
            }
        }

        layout_email_text.isErrorEnabled = false
        mAuth?.sendPasswordResetEmail(emailText)
             ?.addOnCompleteListener(OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    passwordResetSuccess(emailText)
                } else {
                    layout_email_text.isErrorEnabled = true
                    layout_email_text.error = EMAIL_ERROR_TEXT_WRONG
                }
            })

    }

    private fun passwordResetSuccess(emailText: String){
        val intent = Intent(this, ConfirmationActivity::class.java)
        intent.putExtra("message", getString(R.string.reseting_link_has_been_sent_to) + emailText)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
