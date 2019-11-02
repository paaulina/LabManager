package com.example.labmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class TestsResultSearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tests_result_search)
    }

    fun exitActivity(view: View){
        finish()
    }
}
