package com.example.labmanager.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.FragmentManager
import com.example.labmanager.dataBase.dataBaseEntry.UserDataDBEntry

import com.example.labmanager.R
import com.example.labmanager.service.UserTestsResultsGroupManager
import kotlinx.android.synthetic.main.fragment_grouped_results_overview.*
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.labmanager.AUTO_GENERATED
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsInteractor
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsPresenter
import com.example.labmanager.model.UserTestResult

class GroupedResultsOverviewFragment(context: Context,
                                     fragmentManager: FragmentManager
) : Fragment(),
    TestResultsPresenter {

    private var parentContext = context
    private var fragmentmanager = fragmentManager
    private lateinit var listView : ListView
    private lateinit var allTestResults: ArrayList<UserTestResult>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grouped_results_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.groupsListView)
        TestResultsInteractor(
            UserDataDBEntry
        ).getAllTestResults(this)
    }

    override fun presentUsersTestResults(testResults: ArrayList<UserTestResult>) {
        allTestResults = testResults
        setUpResults(testResults)
    }

    override fun presentRetrievalError() {}

    private fun setUpResults(testResults: ArrayList<UserTestResult>){

        val manager = UserTestsResultsGroupManager(testResults)
        val groupsArray = manager.getGroupedByName()
        val names = manager.getGroupNamesArray(groupsArray)
        val adapter = ArrayAdapter<String>(parentContext, R.layout.group_name_recycler_item, names)


        if(groupsListView != null){
            groupsListView.adapter = adapter

            groupsListView.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, position, _ ->

                    val group = groupsArray[position]
                    if(group.groupType == AUTO_GENERATED){
                        val chartFragment = GroupWithChartFragment(groupsArray[position], fragmentmanager, parentContext)
                        fragmentmanager.beginTransaction().replace(R.id.fragments_container, chartFragment)
                                                          .addToBackStack("frag").commit()
                    } else {
                        val noChartFragment = GroupPresenterFragment(parentContext,
                                                                     fragmentmanager,
                                                                     groupsArray[position],
                                                                     allTestResults)
                        fragmentmanager.beginTransaction().replace(R.id.fragments_container, noChartFragment)
                                                          .addToBackStack("frag").commit()
                    }

                }
        }

    }

}
