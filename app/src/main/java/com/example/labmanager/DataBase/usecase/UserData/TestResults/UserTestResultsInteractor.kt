package com.example.labmanager.DataBase.usecase.UserData.TestResults

import com.example.labmanager.DataBase.usecase.UserData.Gateway.UserDataGateway
import com.example.labmanager.Model.UserTestResult

class UserTestResultsInteractor (
    private val userDataGatewey: UserDataGateway
): UserTestResultSavingCallback,
    UserTestResultsGetterCallback {


    lateinit var callBackFunction : (Boolean) -> Unit

    fun saveUserTestResult(
        userTestResult: UserTestResult,
        callbackFunction: (Boolean) -> Unit
    ) {

        callBackFunction = callbackFunction
        userDataGatewey.saveUserTestResult(userTestResult, this)

    }

    fun updateUserTestResult(
        userTestResult: UserTestResult
    ) {
        userDataGatewey.updateUserTestResult(userTestResult, this)
    }

    fun deleteUserTestResult(
        userTestResult: UserTestResult,
        callbackFunction: (Boolean) -> Unit
    ){
        callBackFunction = callBackFunction
        userDataGatewey.deleteUserTestResult(userTestResult, this)
    }



    override fun onSaveSuccess() {
        callBackFunction(true)
    }

    override fun onSaveFailure() {
        callBackFunction(false)
    }

    lateinit var testResultsPresenter: UserTestResultsPresenter;
    fun getAllTestResults(presenter: UserTestResultsPresenter){
        testResultsPresenter = presenter
        userDataGatewey.getUserTestsResults(this)
    }

    override fun onTestResultRetrievalSuccess(testResults: ArrayList<UserTestResult>) {
        testResultsPresenter.presentUsersTestResults(testResults)
    }

    override fun onTestResultRetrievalFailure() {
        testResultsPresenter.presentRetrievalError()
    }

}