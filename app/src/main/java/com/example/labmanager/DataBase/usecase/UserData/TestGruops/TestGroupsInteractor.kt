package com.example.labmanager.DataBase.usecase.UserData.TestGruops

import android.util.Log
import com.example.labmanager.DataBase.usecase.UserData.Gateway.UserDataGateway
import com.example.labmanager.Model.TestsGroup

class TestGroupsInteractor (
    var userDataGateway: UserDataGateway,
    var resultsPresenter: TestGroupsPresenter?,
    var savingPresenter: TestGroupsSavingPresenter?
) : TestGroupsSavingCallback, TestGroupsRetrievalCallback{

    fun getAllUserGroups(){
        userDataGateway.getUserTestsGroup(this)
    }

    fun saveTestGroup(testsGroup: TestsGroup){
        Log.d("savingGroup", "saving")
        userDataGateway.saveUserTestGroup(testsGroup, this)
    }

    fun deleteGroup(group: TestsGroup){
        userDataGateway.deleteUserTestGroup(group, this)
    }

    fun updateGroup(testsGroup: TestsGroup){
        userDataGateway.updateUserTestGroup(testsGroup, this)
    }
    override fun onSaveSuccess() {
        savingPresenter?.onSaveSuccess()
    }

    override fun onSaveFailure() {
        savingPresenter?.onSaveFailure()
    }

    override fun oGroupsRetrievalSuccess(testResults: ArrayList<TestsGroup>) {
        resultsPresenter?.presentTestGroups(testResults)
    }

    override fun onGroupRetrievalFailure() {
        resultsPresenter?.presentRetrievalFailure()
    }




}