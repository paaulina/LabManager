package com.example.labmanager.activity

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
import com.example.labmanager.*
import com.example.labmanager.service.InternetConnectionChecker
import com.example.labmanager.service.TextValidation

import com.example.labmanager.dataBase.dataBaseEntry.UserDataDBEntry
import com.example.labmanager.dataBase.usecase.userData.ProfileData.UserNodeCreationPresenter
import com.example.labmanager.dataBase.usecase.userData.ProfileData.UserNodeInteractor
import com.google.android.gms.tasks.RuntimeExecutionException


class SignupActivity : AppCompatActivity(), UserNodeCreationPresenter {

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
            this,
            resources.getString(R.string.app_cant_work_incorrectly_alert)
        )
    }

    override fun onStart() {
        super.onStart()
        segmentPadding = resources.getDimension(R.dimen.segment_selection_padding).toInt()
        setGenderError(false)
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

    fun getGender(intVal : Int) : String{
        when(intVal){
            0 -> return getString(R.string.female)
            1 -> return getString(R.string.male)
        }
        return getString(R.string.female)
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
                this,
                resources.getString(R.string.set_up_connection_to_continue)
            )
        ){

            mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener{
                try{
                    if(it.isSuccessful){
                        sendConfirmationEmail()
                    } else{
                        var erro = it.result.toString()
                        Toast.makeText(
                            this@SignupActivity, "Authentication failed. $erro",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (err : RuntimeExecutionException){
                    email_edit_text_input_layout.isErrorEnabled = true
                    email_edit_text_input_layout.error = EMAIL_ERROR_TEXT
                }

            }

        }
    }



    private fun sendConfirmationEmail(){

        if(mAuth != null){
            val user = mAuth!!.getCurrentUser()
            user!!.sendEmailVerification().addOnCompleteListener{
                if(it.isSuccessful) {
                    createUserNode(user.uid)
                }
                else {
                    showErrorActivity()
                }
            }
        }

    }



    private fun correctEmailValue(emailText: String): Boolean {
        if(!TextValidation.correctEmailValue(emailText)){
            email_edit_text_input_layout.isErrorEnabled = true
            email_edit_text_input_layout.error = EMAIL_ERROR_TEXT
            return false
        }
        email_edit_text_input_layout.isErrorEnabled = false
        return true
    }

    private fun correctPasswordValue(passwordText: String) : Boolean{
        if(!TextValidation.correctPasswordValue(passwordText)){
            password_edit_text_input_layout.isErrorEnabled = true
            password_edit_text_input_layout.error = PASSWORD_ERROR_TEXT
            return false
        }
        password_edit_text_input_layout.isErrorEnabled = false
        return true
    }

    private fun setGenderError(isError: Boolean){
        val unwrappedDrawable = getDrawable(R.drawable.input_field_error_background)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        if(isError){
            DrawableCompat.setTint(wrappedDrawable, Color.RED)
        } else{
            DrawableCompat.setTint(wrappedDrawable, Color.TRANSPARENT)
        }
        genderSelectionLayout.background = wrappedDrawable
    }

    private fun genderSelected(): Boolean{

        if(selectedGender < 0){
            setGenderError(true)
            return false
        }
        return true
    }

    fun goToLoginActivity(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


    fun goToResetPasswordActivity(view: View){
        val intent = Intent(this, ResetPasswordActivity::class.java)
        intent.putExtra(PASSWOED_ENTRY_POINT, RESET_PASSWORD_ENTRY)
        startActivity(intent)
    }


    fun createUserNode(userId: String){
        UserNodeInteractor(UserDataDBEntry).createUserNode(userId, name_edit_text.text.toString(), getGender(selectedGender), this)
    }

    override fun onNodeCreationSuccess() {
        val intent = Intent(this, ConfirmationActivity::class.java)
        intent.putExtra("message", getString(R.string.registration_finish_successfully))
        startActivity(intent)
    }


    fun showErrorActivity(){
        val intent = Intent(this, ConfirmationActivity::class.java)
        intent.putExtra("message", getString(R.string.something_went_wrong))
        startActivity(intent)
    }

    override fun onNodeCreationFailure() {}
}
