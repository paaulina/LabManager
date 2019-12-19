package com.example.labmanager.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.labmanager.DataBase.DataBaseEntry.UserDataDBEntry
import com.example.labmanager.DataBase.usecase.UserData.ProfileData.UserGlobalPermissionPresenter
import com.example.labmanager.DataBase.usecase.UserData.ProfileData.UserProfileDataInteractor
import com.example.labmanager.GLOBAL_ALLOWED
import com.example.labmanager.GLOBAL_DECLINED
import com.example.labmanager.R
import kotlinx.android.synthetic.main.activity_global_settings.*

class GlobalSettingsActivity : AppCompatActivity(), UserGlobalPermissionPresenter {

    var globalPermission = -1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_settings)

        if(globalPermission == -1){
            setOnLoading()
            UserProfileDataInteractor(UserDataDBEntry).getGlobalPermission(this)
        } else{
            setOffLoading()
            presentGlobalPersmission(globalPermission)
        }


        switch1.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                globalPermission = GLOBAL_DECLINED
            } else {
                globalPermission = GLOBAL_ALLOWED
            }
            UserProfileDataInteractor(UserDataDBEntry).switchGlobal(globalPermission)
        }
    }

    fun setOnLoading(){
        progress_bar_grouped_results.visibility = View.VISIBLE
        switch1.visibility = View.INVISIBLE
        textView10.visibility = View.INVISIBLE
    }

    fun setOffLoading(){
        progress_bar_grouped_results.visibility = View.INVISIBLE
        switch1.visibility = View.VISIBLE
        textView10.visibility = View.VISIBLE
    }


    override fun presentGlobalPersmission(permission: Int) {
        setOffLoading()
        Toast.makeText(this, "Not nullll " , Toast.LENGTH_LONG)
        globalPermission = permission
        when(permission){
            1 -> switch1.isChecked = true
            0 -> switch1.isChecked = false
        }

        Log.d("Checked", "${switch1.isChecked}")
    }

    override fun presentPermissionRetrievalFailure() {
        Toast.makeText(this, "Nullll " , Toast.LENGTH_LONG)
    }

    fun exitActivity(view: View){
        onBackPressed()
    }

    override fun onBackPressed(){
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }
}
