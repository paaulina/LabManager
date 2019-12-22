package com.example.labmanager.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.labmanager.service.InternetConnectionChecker
import com.example.labmanager.R

class NoInternetConnectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet_connection)
    }

    fun checkConnection(view: View){
        if(InternetConnectionChecker.isOnline(this)){
            finish()
        }
    }
}
