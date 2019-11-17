package com.example.labmanager.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.labmanager.R

class SuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
    }

    override fun onBackPressed() {}


    fun addNextResult(view: View){
        val intent = Intent(this, AddTestResultActivity::class.java)
        startActivity(intent)
    }

    fun goBack(view: View){
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }
}
