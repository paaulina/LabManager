package com.example.labmanager.DataBase.usecase.UserData.TestGruops

import com.example.labmanager.Model.TestsGroup

interface TestGroupsPresenter{
    fun presentTestGroups(testResults: ArrayList<TestsGroup>)
    fun presentRetrievalFailure()
}