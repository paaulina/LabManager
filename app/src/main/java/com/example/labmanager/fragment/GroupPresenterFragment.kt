package com.example.labmanager.fragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labmanager.*
import com.example.labmanager.adapter.TestResultAdapter
import com.example.labmanager.dataBase.dataBaseEntry.UserDataDBEntry
import com.example.labmanager.dataBase.usecase.userData.TestGruops.TestGroupsInteractor
import com.example.labmanager.dataBase.usecase.userData.TestGruops.TestGroupsSavingPresenter
import com.example.labmanager.model.TestsGroup
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.service.ItemClickedCallback

class GroupPresenterFragment (
    context: Context,
    fragmentManager: FragmentManager,
    private var testsGroup: TestsGroup,
    private val allResults: ArrayList<UserTestResult>
) : ItemClickedCallback, Fragment(), TestGroupsSavingPresenter{


    private var parentContext = context
    private var fragmentmanager = fragmentManager
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_group_presenter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.resltsRecycler)

        val buttonEdit = view.findViewById<Button>(R.id.button_edit)
        val deleteButton = view.findViewById<ImageButton>(R.id.buttonDelete)
        if(testsGroup.groupType == USER_GENERATED){
            buttonEdit.setOnClickListener {
                val groupEditionFragment = GroupEditionFragment(
                    testsGroup,
                    allResults,
                    fragmentmanager
                )
                fragmentmanager.beginTransaction().replace(R.id.fragments_container, groupEditionFragment).addToBackStack("frag").commit()
            }
            deleteButton.setOnClickListener {
                AlertDialog.Builder(parentContext).setMessage(getString(R.string.do_you_want_toDelete_folder))
                    .setPositiveButton(getString(R.string.yes)) { _, _ -> deleteGroup()}
                    .setNegativeButton(getString(R.string.cancel)){ dialog, _ -> dialog.cancel() }
                    .setCancelable(true)
                    .show()
            }
        } else {
            buttonEdit.visibility = INVISIBLE
            deleteButton.visibility = INVISIBLE
        }




        val buttonBack = view.findViewById<ImageButton>(R.id.button_back)
        buttonBack.setOnClickListener {
            fragmentmanager.popBackStackImmediate()
        }

        view.findViewById<TextView>(R.id.text_view_group_name).text = testsGroup.groupName
        setUpResults()
    }

    private fun deleteGroup(){
        TestGroupsInteractor(UserDataDBEntry, null, this).deleteGroup(testsGroup)
    }

    private fun setUpResults(){
        val recyclerAdapter = TestResultAdapter(testsGroup.resultsList, parentContext, this)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = recyclerAdapter
    }

    override fun itemAtPositionSelected(position: Int) {}

    override fun itemAtPositionLongClicked(position: Int) {}

    override fun onSaveSuccess() {
        fragmentmanager.popBackStackImmediate()
    }

    override fun onSaveFailure() { }

}