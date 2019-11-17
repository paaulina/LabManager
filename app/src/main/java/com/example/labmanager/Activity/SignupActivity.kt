package com.example.labmanager.Activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*
import androidx.core.graphics.drawable.DrawableCompat
import com.example.labmanager.Service.InternetConnectionChecker
import com.example.labmanager.R
import com.example.labmanager.Service.TextValidation


class SignupActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null
    private var selectedGender = -1
    var segmentPadding = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        selectedGender = -1
        mAuth = FirebaseAuth.getInstance()
        InternetConnectionChecker.checkConnection(
            this@SignupActivity,
            this
        )
    }

    override fun onStart() {
        super.onStart()
        segmentPadding = resources.getDimension(R.dimen.segment_selection_padding).toInt()
    }

    fun femaleSegmentSelected(view: View){
        selectSegment(femaleGenderSelector, R.drawable.selected_left_segment)
        deselectSegment(maleGenderSelector,
            R.drawable.unselected_right_segment
        )
        selectedGender = 0
    }

    fun maleSegmentSelected(view: View){
        selectSegment(maleGenderSelector, R.drawable.selected_right_segment)
        deselectSegment(femaleGenderSelector,
            R.drawable.unselected_left_segment
        )
        selectedGender = 1
    }


    fun selectSegment(segment: TextView, backgroundDrawableID: Int){
        genderSelectionLayout.setBackgroundResource(R.drawable.input_field_error_background)
        segment.background = getDrawable(backgroundDrawableID)
        segment.setTextColor(getColor(R.color.buttonColor))
        segment.setPadding(segmentPadding, segmentPadding, segmentPadding, segmentPadding)
    }

    fun deselectSegment(segment: TextView, backgroundDrawableID: Int){
        segment.background = getDrawable(backgroundDrawableID)
        segment.setTextColor(getColor(R.color.buttonCorner))
        segment.setPadding(segmentPadding, segmentPadding, segmentPadding, segmentPadding)
    }


   fun registerUser(view: View){
       var email = email_edit_text.text.toString()
       var password = password_edit_text.text.toString()

        if(correctEmailValue(email) && correctPasswordValue(password) &&
            genderSelected() && InternetConnectionChecker.checkConnection(
                this@SignupActivity,
                this
            )
        ){

            mAuth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth?.getCurrentUser()
                        startApp()
                        //updateUI(user)
                    } else {
                        var erro = task.result.toString()
                        Log.d("DebugPJ: " , erro)
                       Toast.makeText(
                            this@SignupActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    // ...
                }


        }
    }


    fun correctNameValue(nameText: String): Boolean {
        if(!TextValidation.correctNameValue(nameText)){
            name_edit_text_input_layout.isErrorEnabled = true
            name_edit_text_input_layout.error =
                TextValidation.EMAIL_ERROR_TEXT
            return false
        }
        name_edit_text_input_layout.isErrorEnabled = false
        return true
    }

    fun correctEmailValue(emailText: String): Boolean {
        if(!TextValidation.correctEmailValue(emailText)){
            email_edit_text_input_layout.isErrorEnabled = true
            email_edit_text_input_layout.error =
                TextValidation.EMAIL_ERROR_TEXT
            return false
        }
        email_edit_text_input_layout.isErrorEnabled = false
        return true
    }

    fun correctPasswordValue(passwordText: String) : Boolean{
        if(!TextValidation.correctPasswordValue(passwordText)){
            password_edit_text_input_layout.isErrorEnabled = true
            password_edit_text_input_layout.error =
                TextValidation.PASSWORD_ERROR_TEXT
            return false
        }
        password_edit_text_input_layout.isErrorEnabled = false
        return true
    }

    fun genderSelected(): Boolean{
        val unwrappedDrawable = getDrawable(R.drawable.input_field_error_background)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, Color.RED)
        if(selectedGender < 0){
            genderSelectionLayout.background = wrappedDrawable
            return false
        }
        return true
    }

    fun goToLoginActivity(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    fun startApp(){
        val intent = Intent(this, ConfirmationActivity::class.java)
        intent.putExtra("message", "Rejestracja zakończona")
        startActivity(intent)
    }

    fun goToResetPasswordActivity(view: View){
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }
}
