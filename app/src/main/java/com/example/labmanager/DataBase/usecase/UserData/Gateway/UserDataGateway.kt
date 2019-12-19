package com.example.labmanager.DataBase.usecase.UserData.Gateway

import com.example.labmanager.DataBase.usecase.MedicalFiles.MedicalFilesSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.MedicalFiles.MedicalFilesIDsRetrievalCallback
import com.example.labmanager.DataBase.usecase.UserData.MedicalFiles.MedicalFilesIDsSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.ProfileData.UserDataCallback
import com.example.labmanager.DataBase.usecase.UserData.TestGruops.TestGroupsRetrievalCallback
import com.example.labmanager.DataBase.usecase.UserData.TestGruops.TestGroupsSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.TestResults.TestResultsSavingCallback
import com.example.labmanager.DataBase.usecase.UserData.TestResults.TestResultsRetrievalCallback
import com.example.labmanager.Model.TestsGroup
import com.example.labmanager.Model.UserTestResult

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

    fun saveUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback)
    fun updateUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback)
    fun deleteUserTestGroup(testsGroup: TestsGroup, callback: TestGroupsSavingCallback)
    fun getUserTestsGroup(callback: TestGroupsRetrievalCallback)

    fun addMedicalFileID(name: String, callback: MedicalFilesIDsSavingCallback)
    fun removeMedicalFileID(id: String, callback: MedicalFilesIDsSavingCallback)
    fun getAllMadicalFilesIDs(callback: MedicalFilesIDsRetrievalCallback)


}