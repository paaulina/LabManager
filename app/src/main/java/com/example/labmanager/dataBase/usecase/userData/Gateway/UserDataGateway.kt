package com.example.labmanager.dataBase.usecase.userData.Gateway

import com.example.labmanager.dataBase.usecase.userData.MedicalFiles.MedicalFilesIDsRetrievalCallback
import com.example.labmanager.dataBase.usecase.userData.MedicalFiles.MedicalFilesIDsSavingCallback
import com.example.labmanager.dataBase.usecase.userData.ProfileData.UserDataCallback
import com.example.labmanager.dataBase.usecase.userData.TestGruops.TestGroupsRetrievalCallback
import com.example.labmanager.dataBase.usecase.userData.TestGruops.TestGroupsSavingCallback
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsSavingCallback
import com.example.labmanager.dataBase.usecase.userData.TestResults.TestResultsRetrievalCallback
import com.example.labmanager.model.TestsGroup
import com.example.labmanager.model.UserTestResult

interface UserDataGateway {
    fun saveUserTestResult(userTestResult: UserTestResult, callback: TestResultsSavingCallback)
    fun updateUserTestResult(userTestResult: UserTestResult, callback: TestResultsSavingCallback)
    fun deleteUserTestResult(userTestResult: UserTestResult, callback: TestResultsSavingCallback)
    fun getUserTestsResults(callback: TestResultsRetrievalCallback)
    fun setFavourite(testID: String, newFavValue: Int, callback: TestResultsSavingCallback)

    fun createUserNode(userID: String, name: String, gender: String, globalPermission: Int, callback: UserDataCallback)
   // fun checkUserNode(callback: UserDataCallback)
    fun getGlobalPermission(callback: UserDataCallback)
    fun allowGlobal(callback: UserDataCallback)
    fun disableGlobal(callback: UserDataCallback)

    fun saveUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback?)
    fun updateUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback?)
    fun deleteUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback)
    fun getUserTestsGroup(callback: TestGroupsRetrievalCallback)

    fun addMedicalFileID(name: String, callback: MedicalFilesIDsSavingCallback)
    fun removeMedicalFileID(id: String, callback: MedicalFilesIDsSavingCallback)
    fun getAllMadicalFilesIDs(callback: MedicalFilesIDsRetrievalCallback)


}