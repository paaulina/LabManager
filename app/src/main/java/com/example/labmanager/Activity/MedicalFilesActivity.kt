package com.example.labmanager.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.labmanager.R

class MedicalFilesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_files)
    }

    fun exitActivity(view: View){
        finish()
    }

    override fun onBackPressed(){}
}
