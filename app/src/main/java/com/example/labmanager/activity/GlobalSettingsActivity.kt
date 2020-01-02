package com.example.labmanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.labmanager.dataBase.dataBaseEntry.UserDataDBEntry
import com.example.labmanager.dataBase.usecase.userData.ProfileData.UserGlobalPermissionPresenter
import com.example.labmanager.dataBase.usecase.userData.ProfileData.UserNodeInteractor
import com.example.labmanager.GLOBAL_ALLOWED
import com.example.labmanager.GLOBAL_DECLINED
import com.example.labmanager.R
import com.example.labmanager.service.InternetConnectionChecker
import kotlinx.android.synthetic.main.activity_global_settings.*

class GlobalSettingsActivity : AppCompatActivity(), UserGlobalPermissionPresenter {

    private var globalPermission = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_settings)

        if(globalPermission == -1){
            setOnLoading()
            UserNodeInteractor(UserDataDBEntry).getGlobalPermission(this)
        } else{
            setOffLoading()
            presentGlobalPersmission(globalPermission)
        }


        switch1.setOnCheckedChangeListener { _, isChecked ->
            globalPermission = if(isChecked){
                GLOBAL_DECLINED
            } else {
                GLOBAL_ALLOWED
            }
            UserNodeInteractor(UserDataDBEntry).switchGlobal(globalPermission)
        }
        InternetConnectionChecker.checkConnection(
            this,
            this,
            resources.getString(R.string.app_cant_work_incorrectly_alert)
        )
    }

    private fun setOnLoading(){
        progress_bar_grouped_results.visibility = View.VISIBLE
        switch1.visibility = View.INVISIBLE
        textView10.visibility = View.INVISIBLE
    }

    private fun setOffLoading(){
        progress_bar_grouped_results.visibility = View.INVISIBLE
        switch1.visibility = View.VISIBLE
        textView10.visibility = View.VISIBLE
    }


    override fun presentGlobalPersmission(permission: Int) {
        setOffLoading()
        globalPermission = permission
        when(permission){
            1 -> switch1.isChecked = true
            0 -> switch1.isChecked = false
        }
    }

    override fun presentPermissionRetrievalFailure() {}

    fun exitActivity(view: View){
        onBackPressed()
    }

    override fun onBackPressed(){
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }
}
