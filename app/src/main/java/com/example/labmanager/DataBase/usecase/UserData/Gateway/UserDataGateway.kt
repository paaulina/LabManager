package com.example.labmanager.DataBase.usecase.UserData.Gateway

import com.example.labmanager.DataBase.usecase.UserData.ProfileData.UserDataCallback
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.TestResults.UserTestResultsGetterCallback
import com.example.labmanager.Model.UserTestResult

interface UserDataGateway {
    fun saveUserTestResult(userTestResult: UserTestResult, callback: UserTestResultSavingCallback)
    fun updateUserTestResult(userTestResult: UserTestResult, callback: UserTestResultSavingCallback)
    fun deleteUserTestResult(userTestResult: UserTestResult, callback: UserTestResultSavingCallback)
    fun getUserTestsResults(callback: UserTestResultsGetterCallback)

    fun createUserNode(name: String, gender: String, globalPermission: Int, callback: UserDataCallback)
    fun checkUserNode(callback: UserDataCallback)
    fun getGlobalPermission(callback: UserDataCallback)
    fun allowGlobal(callback: UserDataCallback)
    fun disableGlobal(callback: UserDataCallback)


}