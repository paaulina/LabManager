package com.example.labmanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.labmanager.*
import com.example.labmanager.dataBase.dataBaseEntry.StorageEntry
import com.example.labmanager.dataBase.dataBaseEntry.UserDataDBEntry
import com.example.labmanager.service.DummyBitmap
import com.example.labmanager.service.InternetConnectionChecker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_page.*

class MainPageActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)
        mAuth = FirebaseAuth.getInstance()
        DummyBitmap.initialize(this)
        InternetConnectionChecker.checkConnection(
            this,
            this,
            resources.getString(com.example.labmanager.R.string.app_cant_work_incorrectly_alert)
        )
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
            globalSettongsSubButton.visibility = View.VISIBLE
        } else {
            changePasswordSubButton.visibility = View.GONE
            logOutSubButton.visibility = View.GONE
            globalSettongsSubButton.visibility = View.GONE
        }
    }


    fun goToAddTestResultActivity(view: View){
        val intent = Intent(this, AddTestResultActivity::class.java)
        startActivity(intent)
    }

    fun goToAddMedicalFilesActivity(view: View){
        val intent = Intent(this, AddMedicalFilesActivity::class.java)
        startActivity(intent)
    }

    fun goToTestsResultsSearchActivity(view: View){
        val intent = Intent(this, TestsResultOverviewActivity::class.java)
        startActivity(intent)
    }

    fun goToMedicalFilesActivity(view: View){
        val intent = Intent(this, MedicalFilesActivity::class.java)
        startActivity(intent)
    }

    fun goToPlannerActivity(view: View){}

    fun goToChangePasswordrActivity(view: View){
        val intent = Intent(this, ResetPasswordActivity::class.java)
        intent.putExtra(PASSWOED_ENTRY_POINT, CHANGE_PASSWORD_ENTRY)
        startActivity(intent)
    }

    fun logOut (view : View){
        mAuth?.signOut()!!
        UserDataDBEntry.destroy()
        StorageEntry.destroy()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun goToGlobalSettingActivity (view : View){

        val intent = Intent(this, GlobalSettingsActivity::class.java)
        startActivity(intent)
    }


}
