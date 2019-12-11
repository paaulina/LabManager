package com.example.labmanager.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.labmanager.Fragment.GroupedResultsOverviewFragment
import com.example.labmanager.Fragment.TestsResultsOverviewFragment
import com.example.labmanager.Fragment.UserGroupsOverviewFragment
import com.example.labmanager.R
import kotlinx.android.synthetic.main.activity_tests_result_overview.*

class TestsResultOverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tests_result_overview)


        val testsResultsOverviewFragment = TestsResultsOverviewFragment(this)
        val userGroupsOverviewFragment = UserGroupsOverviewFragment(this, supportFragmentManager)
        val groupedResultsOverviewFragment = GroupedResultsOverviewFragment(this, supportFragmentManager)

        bottom_navigation.selectedItemId = R.id.nav_all_results_overview

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragments_container, testsResultsOverviewFragment)
            .add(R.id.fragments_container, userGroupsOverviewFragment)
            .add(R.id.fragments_container, groupedResultsOverviewFragment)
            .hide(userGroupsOverviewFragment)
            .hide(groupedResultsOverviewFragment)
            .commit()

        bottom_navigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_all_results_overview -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(userGroupsOverviewFragment)
                        .hide(groupedResultsOverviewFragment)
                        .show(testsResultsOverviewFragment)
                        .commit()

                    Log.d("LogDebug1", "LogDebug1")
                }
                R.id.nav_user_groups_overview -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(testsResultsOverviewFragment)
                        .hide(groupedResultsOverviewFragment)
                        .show(userGroupsOverviewFragment)
                        .commit()
                    Log.d("LogDebug2", "LogDebug2")
                }
                R.id.nav_grouped_results_overview -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(testsResultsOverviewFragment)
                        .hide(userGroupsOverviewFragment)
                        .show(groupedResultsOverviewFragment)
                        .commit()
                    Log.d("LogDebug3", "LogDebug3")

                    //(supportFragmentManager.findFragmentById(R.id.fragments_container) as GroupedResultsOverviewFragment).setUpResults()
                }
                else -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(testsResultsOverviewFragment)
                        .commit()
                    Log.d("LogDebug", "LogDebug")
                }
            }

            true
        }

    }

    fun exitActivity(view: View){
        finish()
    }
}
