package com.example.labmanager.DataBase.usecase.UserData.UserTestResultsSaving

import com.example.labmanager.Model.UserTestResult

interface UserTestResutSavingGatewey {
    fun saveUserTestResult(userTestResult: UserTestResult, callback: UserTestResultSavingCallback)
}