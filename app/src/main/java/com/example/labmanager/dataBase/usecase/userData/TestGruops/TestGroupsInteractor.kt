package com.example.labmanager.dataBase.usecase.userData.TestGruops

import android.util.Log
import com.example.labmanager.dataBase.usecase.userData.Gateway.UserDataGateway
import com.example.labmanager.model.TestsGroup

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