package com.example.labmanager.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.labmanager.fragment.GroupedResultsOverviewFragment
import com.example.labmanager.fragment.TestsResultsOverviewFragment
import com.example.labmanager.fragment.UserGroupsOverviewFragment
import com.example.labmanager.R
import com.example.labmanager.service.InternetConnectionChecker
import kotlinx.android.synthetic.main.activity_tests_result_overview.*

class TestsResultOverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tests_result_overview)


        val testsResultsOverviewFragment = TestsResultsOverviewFragment(this)
        val userGroupsOverviewFragment = UserGroupsOverviewFragment(this, supportFragmentManager)
        val groupedResultsOverviewFragment = GroupedResultsOverviewFragment(this, supportFragmentManager)

        if(!InternetConnectionChecker.isOnline(this)){
            val intent = Intent(this, NoInternetConnectionActivity::class.java)
            startActivity(intent)
        }

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
            while(supportFragmentManager.backStackEntryCount > 0){
                supportFragmentManager.popBackStackImmediate()
            }

            when(it.itemId) {
                R.id.nav_all_results_overview -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(userGroupsOverviewFragment)
                        .hide(groupedResultsOverviewFragment)
                        .show(testsResultsOverviewFragment)
                        .commit()


                }
                R.id.nav_user_groups_overview -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(testsResultsOverviewFragment)
                        .hide(groupedResultsOverviewFragment)
                        .show(userGroupsOverviewFragment)
                        .commit()
                }
                R.id.nav_grouped_results_overview -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(testsResultsOverviewFragment)
                        .hide(userGroupsOverviewFragment)
                        .show(groupedResultsOverviewFragment)
                        .commit()
                }
                else -> {
                    supportFragmentManager
                        .beginTransaction()
                        .hide(testsResultsOverviewFragment)
                        .commit()
                }
            }

            true
        }

    }

    fun exitActivity(view: View){
        val intent = Intent(this, MainPageActivity::class.java)
        startActivity(intent)
    }
}
