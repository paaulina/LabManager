package com.example.labmanager.dataBase.usecase.userData.TestGruops

import com.example.labmanager.model.TestsGroup

interface TestGroupsRetrievalCallback {
    fun oGroupsRetrievalSuccess(testResults: ArrayList<TestsGroup>)
    fun onGroupRetrievalFailure()
}