package com.example.labmanager.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.FragmentManager
import com.example.labmanager.DataBase.DataBaseEntry.UserDataDBEntry

import com.example.labmanager.R
import com.example.labmanager.Service.UserTestsResultsGroupManager
import kotlinx.android.synthetic.main.fragment_grouped_results_overview.*
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultsInteractor
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultsPresenter
import com.example.labmanager.Model.UserTestResult

class GroupedResultsOverviewFragment(context: Context, fragmentManager: FragmentManager ) : Fragment(),
    UserTestResultsPresenter {

    var parentContext = context
    var fragmentmanager = fragmentManager
    lateinit var listView : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        UserTestResultsInteractor(
            UserDataDBEntry
        ).getAllTestResults(this)
    }

    override fun presentUsersTestResults(testResults: ArrayList<UserTestResult>) {
        setUpResults(testResults)
    }

    override fun presentRetrievalError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setUpResults(testResults: ArrayList<UserTestResult>){

        var manager = UserTestsResultsGroupManager(testResults)
        var groupsArray = manager.getGroupedByName()
        var names = manager.getGroupNamesArray(groupsArray)
        var adapter = ArrayAdapter<String>(parentContext, R.layout.simple_spinner_item, names)


        if(groupsListView != null){
            groupsListView.adapter = adapter

            groupsListView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                var chartFragment = GroupWithChartFragment(groupsArray.get(position), fragmentmanager, parentContext)
                fragmentmanager.beginTransaction().replace(R.id.fragments_container, chartFragment).addToBackStack("frag").commit()
            })
        }

    }

    fun showErrorMessage(msg: String){

    }

}
