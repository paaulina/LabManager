package com.example.labmanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.labmanager.MED_FILE_ADDITION
import com.example.labmanager.R
import com.example.labmanager.SUCCESS_ACTIVITY_ENTRY_POINT
import com.example.labmanager.TEST_RESULT_ADITION
import kotlinx.android.synthetic.main.activity_success.*


class SuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)

        val extras = intent.extras

        if (extras != null) {
            when(extras.get(SUCCESS_ACTIVITY_ENTRY_POINT) as Int){
                TEST_RESULT_ADITION -> {}
                MED_FILE_ADDITION -> {
                    textViewMessage.text = getString(R.string.med_file_has_been_successfully_Added)
                    button_add_next.visibility = View.GONE
                }
            }
        }
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
