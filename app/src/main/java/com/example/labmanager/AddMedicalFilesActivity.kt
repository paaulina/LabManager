package com.example.labmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AddMedicalFilesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medical_files)
    }

    fun exitActivity(view: View){
        finish()
    }

    override fun onBackPressed(){}

}
