package com.example.labmanager.Fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.FragmentManager
import com.example.labmanager.DataBase.DataBaseEntry.UserDataDBEntry
import com.example.labmanager.DataBase.usecase.UserData.TestGruops.TestGroupsInteractor
import com.example.labmanager.DataBase.usecase.UserData.TestGruops.TestGroupsPresenter
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultsInteractor
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultsPresenter
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.Model.UserTestResult

import com.example.labmanager.R
import com.example.labmanager.Service.UserTestsResultsGroupManager
import kotlinx.android.synthetic.main.fragment_grouped_results_overview.*
import kotlinx.android.synthetic.main.fragment_user_groups_overview.*

class UserGroupsOverviewFragment (
    context: Context,
    fragmentManager: FragmentManager
) : Fragment(),
    UserTestResultsPresenter,
    TestGroupsPresenter{

    var parentContext = context
    var fragmentmanager = fragmentManager
    var allTestResults = arrayListOf<UserTestResult>()
    var allGroups = arrayListOf<TestsGroup>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("logDebug", "logdebug created 2")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_user_groups_overview,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UserTestResultsInteractor(
            UserDataDBEntry
        ).getAllTestResults(this)
    }

    override fun presentUsersTestResults(testResults: ArrayList<UserTestResult>) {
        allTestResults = testResults
        TestGroupsInteractor(
            UserDataDBEntry,
            this
        ).getAllUserGroups()
    }

    override fun presentRetrievalError() {

    }

    override fun presentTestGroups(testResults: ArrayList<TestsGroup>) {
        allGroups = testResults
        progress_bar.visibility = View.INVISIBLE
        setUpResults(testResults)
    }

    override fun presentRetrievalFailure() {}

    fun setUpResults(testGroups: ArrayList<TestsGroup>){

        var names = arrayListOf<String>()
        for(group in testGroups){
            names.add(group.groupName)
        }
        var adapter = ArrayAdapter<String>(parentContext, R.layout.simple_spinner_item, names)


        if(userGroupsListView != null){
            userGroupsListView.adapter = adapter

            userGroupsListView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                var groupPresenterFragment = GroupPresenterFragment(parentContext, fragmentmanager, testGroups.get(position),  allTestResults)
                fragmentmanager.beginTransaction().replace(R.id.fragments_container, groupPresenterFragment).addToBackStack("frag").commit()
            })
        }

    }

}
