package com.example.labmanager.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labmanager.Adapter.SelectiveTestResult.SelectiveTestResultAdapter
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.R
import kotlinx.android.synthetic.main.activity_group_edition.*

class GroupEditionActivity(
    var testsGroup: TestsGroup,
    val allResults: ArrayList<UserTestResult>
) : AppCompatActivity() {

    lateinit var adapter: SelectiveTestResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_edition)

        buttonSave.setOnClickListener {
            var finalGroup = adapter.getFinalTestResultsArray()
            //TODO saving
        }

        buttonCancel.setOnClickListener {
            //todo cancelling
        }
    }

    fun setUpRecycler(){
        adapter = SelectiveTestResultAdapter(allResults, testsGroup, this)
        resltsRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        resltsRecycler.setHasFixedSize(true)
        resltsRecycler.adapter = adapter
    }
}
