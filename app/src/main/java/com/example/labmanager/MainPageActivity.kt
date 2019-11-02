package com.example.labmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_page.*

class MainPageActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        mAuth = FirebaseAuth.getInstance()

        if(mAuth!!.currentUser != null){
            Log.d("Debug:" , " user is logged")
        }else{
            Log.d("Debug:" , " user is not logged")
        }

        DataBaseManager.initArray()
    }

    override fun onBackPressed(){}

    fun updateAddSubbutonsVisibility(view: View){
        if(addTextResultSubbutton.visibility == View.GONE){
            addTextResultSubbutton.visibility = View.VISIBLE
            addMedicalFileSubbButton.visibility = View.VISIBLE
        } else {
            addTextResultSubbutton.visibility = View.GONE
            addMedicalFileSubbButton.visibility = View.GONE
        }


    }

    fun updateSettingsSubbutonsVisibility(view: View){
        if(changePasswordSubButton.visibility == View.GONE){
            changePasswordSubButton.visibility = View.VISIBLE
            logOutSubButton.visibility = View.VISIBLE
        } else {
            changePasswordSubButton.visibility = View.GONE
            logOutSubButton.visibility = View.GONE
        }
    }


    fun goToAddTestResultActivity(view: View){
        val intent = Intent(this, AddTestResultActivity::class.java)
        //intent.putExtra("keyIdentifier", value)
        startActivity(intent)
    }

    fun goToAddMedicalFilesActivity(view: View){
        val intent = Intent(this, AddMedicalFilesActivity::class.java)
        //intent.putExtra("keyIdentifier", value)
        startActivity(intent)
    }

    fun goToTestsResultsSearchActivity(view: View){
        val intent = Intent(this, TestsResultSearchActivity::class.java)
        //intent.putExtra("keyIdentifier", value)
        startActivity(intent)
    }

    fun goToMedicalFilesActivity(view: View){

    }

    fun goToPlannerActivity(view: View){

    }

    fun goToChangePasswordrActivity(view: View){
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivity(intent)
    }

    fun logOut (view : View){
        mAuth?.signOut()!!
        val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("keyIdentifier", value)
        startActivity(intent)
    }
}