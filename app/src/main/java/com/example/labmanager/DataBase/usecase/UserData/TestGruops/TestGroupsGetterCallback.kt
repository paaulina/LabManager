package com.example.labmanager.DataBase.usecase.UserData.TestGruops

import com.example.labmanager.Model.TestsGroup

interface TestGroupsGetterCallback {
    fun oGroupsRetrievalSuccess(testResults: ArrayList<TestsGroup>)
    fun onGroupRetrievalFailure()
}