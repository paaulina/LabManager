package com.example.labmanager.dataBase.usecase.userData.TestResults

import com.example.labmanager.dataBase.usecase.userData.Gateway.UserDataGateway
import com.example.labmanager.IS_FAVORITE
import com.example.labmanager.model.UserTestResult
import com.example.labmanager.NOT_FAVORITE
import com.example.labmanager.dataBase.usecase.userData.TestGruops.TestGroupsSavingCallback

class TestResultsInteractor (
    private val userDataGatewey: UserDataGateway
): TestResultsSavingCallback,
    TestResultsRetrievalCallback {


    //lateinit var callBackFunction : (Boolean) -> Unit
    lateinit var savingPresenter : TestsResultSavingPresenter
    fun saveUserTestResult(
        userTestResult: UserTestResult,
        testsResultSavingPresenter: TestsResultSavingPresenter
    ) {
        savingPresenter = testsResultSavingPresenter
        userDataGatewey.saveUserTestResult(userTestResult, this)
    }

    fun updateUserTestResult(
        userTestResult: UserTestResult,
        testsResultSavingPresenter: TestsResultSavingPresenter
    ) {
        savingPresenter = testsResultSavingPresenter
        userDataGatewey.updateUserTestResult(userTestResult, this)
    }

    fun setFavourite(userTestResult: UserTestResult, testsResultSavingPresenter: TestsResultSavingPresenter){
        savingPresenter = testsResultSavingPresenter
        userDataGatewey.setFavourite(userTestResult.id, IS_FAVORITE, this)
    }

    fun setUNFavourite(userTestResult: UserTestResult, testsResultSavingPresenter: TestsResultSavingPresenter){
        savingPresenter = testsResultSavingPresenter
        userDataGatewey.setFavourite(userTestResult.id, NOT_FAVORITE, this)
    }

    fun deleteUserTestResult(
        userTestResult: UserTestResult,
        testsResultSavingPresenter: TestsResultSavingPresenter
    ){
        savingPresenter = testsResultSavingPresenter
        userDataGatewey.deleteUserTestResult(userTestResult, this)
    }



    override fun onSaveSuccess() {
        savingPresenter.presentSaveSuccess()
    }

    override fun onSaveFailure() {
        savingPresenter.presentSaveError()
    }

    lateinit var testResultsPresenter: TestResultsPresenter;
    fun getAllTestResults(presenter: TestResultsPresenter){
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