package com.example.labmanager.dataBase.usecase.userData.TestResults

import com.example.labmanager.model.UserTestResult

interface TestResultsPresenter{
    fun presentUsersTestResults(testResults: ArrayList<UserTestResult>)
    fun presentRetrievalError()
}