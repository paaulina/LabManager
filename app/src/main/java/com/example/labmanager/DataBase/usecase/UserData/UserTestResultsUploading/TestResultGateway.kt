package com.example.labmanager.DataBase.usecase.UserData.UserTestResultsUploading

interface TestResultGateway{
    //fun saveUserTestResult(userTestResult: UserTestResult, callback: TestResultCallback)
    fun retrieveAllTestResultsForUser(callback: TestResultCallback)


}