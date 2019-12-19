package com.example.labmanager.DataBase.usecase.UserData.TestGruops

import com.example.labmanager.Model.TestsGroup

interface TestGroupsRetrievalCallback {
    fun oGroupsRetrievalSuccess(testResults: ArrayList<TestsGroup>)
    fun onGroupRetrievalFailure()
}