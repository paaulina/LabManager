package com.example.labmanager.DataBase.usecase.UserData.TestGruops

import com.example.labmanager.DataBase.usecase.UserData.Gateway.UserDataGateway
import com.example.labmanager.Model.TestsGroup

class TestGroupsInteractor (
    var userDataGateway: UserDataGateway,
    var presenter: TestGroupsPresenter
) : TestGroupsSavingCallback, TestGroupsGetterCallback{

    fun getAllUserGroups(){
        userDataGateway.getUserTestsGroup(this)
    }
    override fun onSaveSuccess() {

    }

    override fun onSaveFailure() {

    }

    override fun oGroupsRetrievalSuccess(testResults: ArrayList<TestsGroup>) {
        presenter.presentTestGroups(testResults)
    }

    override fun onGroupRetrievalFailure() {
        presenter.presentRetrievalFailure()
    }


}