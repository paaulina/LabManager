package com.example.labmanager.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.FragmentManager
import com.example.labmanager.dataBase.dataBaseEntry.UserDataDBEntry
import com.example.labmanager.dataBase.usecase.userData.TestGruops.TestGroupsInteractor
import com.example.labmanager.dataBase.usecase.userData.TestGruops.TestGroupsPresenter
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsInteractor
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsPresenter
import com.example.labmanager.model.TestsGroup
import com.example.labmanager.model.UserTestResult

import com.example.labmanager.R
import kotlinx.android.synthetic.main.fragment_user_groups_overview.*
import android.widget.AdapterView.OnItemLongClickListener


class UserGroupsOverviewFragment (
    context: Context,
    fragmentManager: FragmentManager
) : Fragment(),
    TestResultsPresenter,
    TestGroupsPresenter{

    private var parentContext = context
    private var fragmentmanager = fragmentManager
    private var allTestResults = arrayListOf<UserTestResult>()
    private var allGroups = arrayListOf<TestsGroup>()
    private lateinit var addButton : Button

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


        addButton = view.findViewById(R.id.button_add_group)
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

    override fun presentRetrievalError() {}

    private fun addGroup(){
        if(allTestResults.size > 0){
            val groupEditionFragment = GroupEditionFragment(
                TestsGroup(),
                allTestResults,
                fragmentmanager
            )
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

    private fun setUpResults(testGroups: ArrayList<TestsGroup>){

        val names = arrayListOf<String>()
        for(group in testGroups){
            names.add(group.groupName)
        }
        val adapter = ArrayAdapter<String>(parentContext, R.layout.group_name_recycler_item, names)


        if(userGroupsListView != null){
            userGroupsListView.adapter = adapter

            userGroupsListView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->
                    val groupPresenterFragment = GroupPresenterFragment(parentContext,
                                                                        fragmentmanager,
                                                                        testGroups[position],
                                                                        allTestResults)
                    fragmentmanager.beginTransaction()
                                    .replace(R.id.fragments_container, groupPresenterFragment)
                                    .addToBackStack("frag").commit()
                }



            userGroupsListView.onItemLongClickListener =
                OnItemLongClickListener { _, _, _, _ ->
                    true
                }
        }

    }

}
