package com.example.labmanager.Fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labmanager.Adapter.SelectiveTestResult.SelectiveTestResultAdapter
import com.example.labmanager.DataBase.DataBaseEntry.UserDataDBEntry
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultsInteractor
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.Model.UserTestResult
import com.example.labmanager.R
import kotlinx.android.synthetic.main.activity_group_edition.*
import kotlinx.android.synthetic.main.fragment_group_edition.*
import kotlinx.android.synthetic.main.fragment_group_edition.buttonCancel
import kotlinx.android.synthetic.main.fragment_group_edition.buttonSave
import kotlinx.android.synthetic.main.fragment_group_edition.resltsRecycler
import kotlinx.android.synthetic.main.fragment_tests_results_overview.*

class GroupEditionFragment (
    context: Context,
    var testsGroup: TestsGroup,
    val allResults: ArrayList<UserTestResult>
) : Fragment(){

    lateinit var adapter: SelectiveTestResultAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_edition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()

        val editTextName = view.findViewById<EditText>(R.id.name_edit_text)
        editTextName.setText(testsGroup.groupName)

        buttonSave.setOnClickListener {

        }

        buttonCancel.setOnClickListener {

        }
    }
    fun setUpRecycler(){
        adapter = SelectiveTestResultAdapter(allResults, testsGroup, context!!)
        resltsRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        resltsRecycler.setHasFixedSize(true)
        resltsRecycler.adapter = adapter
    }

}