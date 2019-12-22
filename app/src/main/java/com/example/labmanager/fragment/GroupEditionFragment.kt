package com.example.labmanager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labmanager.adapter.selectiveTestResult.SelectiveTestResultAdapter
import com.example.labmanager.dataBase.dataBaseEntry.UserDataDBEntry
import com.example.labmanager.dataBase.usecase.userData.TestGruops.TestGroupsInteractor
import com.example.labmanager.dataBase.usecase.userData.TestGruops.TestGroupsSavingPresenter
import com.example.labmanager.model.TestsGroup
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.R
import kotlinx.android.synthetic.main.fragment_group_edition.buttonCancel
import kotlinx.android.synthetic.main.fragment_group_edition.buttonSave
import kotlinx.android.synthetic.main.fragment_group_edition.resltsRecycler

class GroupEditionFragment(
    private var testsGroup: TestsGroup,
    private val allResults: ArrayList<UserTestResult>,
    private val fragmentmanager: FragmentManager
) : Fragment(), TestGroupsSavingPresenter{

    private var isGroupEdition = false
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

        if(testsGroup.groupName.isNotEmpty()) {
            isGroupEdition = true
        }
        setUpRecycler()

        val editTextName = view.findViewById<EditText>(R.id.name_edit_text)
        editTextName.setText(testsGroup.groupName)

        buttonSave.setOnClickListener {
            if(editTextName.text.toString().isNotEmpty()){
                val testsGroup = adapter.testsGroup
                testsGroup.groupName = editTextName.text.toString()
                if(isGroupEdition){
                    TestGroupsInteractor(UserDataDBEntry, null, this).updateGroup(testsGroup)
                } else{
                    TestGroupsInteractor(UserDataDBEntry, null, this).saveTestGroup(testsGroup)
                }
            }
        }

        buttonCancel.setOnClickListener {
            fragmentmanager.popBackStackImmediate()
        }
    }


    private fun setUpRecycler(){
        adapter = SelectiveTestResultAdapter(allResults, testsGroup, context!!)
        resltsRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        resltsRecycler.setHasFixedSize(true)
        resltsRecycler.adapter = adapter
    }

    override fun onSaveSuccess() {
        fragmentmanager.popBackStackImmediate()
    }

    override fun onSaveFailure() {}

}