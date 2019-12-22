package com.example.labmanager.dataBase.usecase.userData.TestGruops

import com.example.labmanager.model.TestsGroup

interface TestGroupsPresenter{
    fun presentTestGroups(testResults: ArrayList<TestsGroup>)
    fun presentRetrievalFailure()
}