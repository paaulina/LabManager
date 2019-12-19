package com.example.labmanager.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.example.labmanager.DataBase.DataBaseEntry.UserDataDBEntry
import com.example.labmanager.DataBase.usecase.UserData.TestGruops.TestGroupsInteractor
import com.example.labmanager.DataBase.usecase.UserData.TestGruops.TestGroupsPresenter
import com.example.labmanager.DataBase.usecase.UserData.TestResults.TestResultsInteractor
import com.example.labmanager.DataBase.usecase.UserData.TestResults.TestResultsPresenter
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.Model.UserTestResult

import com.example.labmanager.R
import kotlinx.android.synthetic.main.fragment_user_groups_overview.*
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ImageButton


class UserGroupsOverviewFragment (
    context: Context,
    fragmentManager: FragmentManager
) : Fragment(),
    TestResultsPresenter,
    TestGroupsPresenter{

    var parentContext = context
    var fragmentmanager = fragmentManager
    var allTestResults = arrayListOf<UserTestResult>()
    var allGroups = arrayListOf<TestsGroup>()
    lateinit var addButton : Button
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
        TestResultsInteractor(
            UserDataDBEntry
        ).getAllTestResults(this)


        addButton = view.findViewById<Button>(R.id.button_add_group)
        addButton.setOnClickListener {
            addGroup()
        }

    }

    override fun presentUsersTestResults(testResults: ArrayList<UserTestResult>) {
        allTestResults = testResults
        TestGroupsInteractor(
            UserDataDBEntry,
            this,
            null
        ).getAllUserGroups()
    }

    override fun presentRetrievalError() {

    }

    fun addGroup(){
        if(allTestResults.size > 0){
            var groupEditionFragment = GroupEditionFragment(parentContext, TestsGroup(),  allTestResults, fragmentmanager)
            fragmentmanager.beginTransaction().replace(R.id.fragments_container, groupEditionFragment).addToBackStack("frag").commit()

        }
    }
    override fun presentTestGroups(testResults: ArrayList<TestsGroup>) {
        allGroups = testResults
        if(progress_bar_user_groups != null)
            progress_bar_user_groups.visibility = View.INVISIBLE
        setUpResults(testResults)
    }

    override fun presentRetrievalFailure() {}

    fun setUpResults(testGroups: ArrayList<TestsGroup>){

        var names = arrayListOf<String>()
        for(group in testGroups){
            names.add(group.groupName)
        }
        var adapter = ArrayAdapter<String>(parentContext, R.layout.group_name_recycler_item, names)


        if(userGroupsListView != null){
            userGroupsListView.adapter = adapter

            userGroupsListView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                var groupPresenterFragment = GroupPresenterFragment(parentContext, fragmentmanager, testGroups.get(position),  allTestResults)
                fragmentmanager.beginTransaction().replace(R.id.fragments_container, groupPresenterFragment).addToBackStack("frag").commit()
            })



            userGroupsListView.setOnItemLongClickListener(OnItemLongClickListener { arg0, arg1, pos, id ->
                // TODO Auto-generated method stub

                Log.v("long clicked exoo", "pos: $pos")

                true
            })
        }

    }

}
