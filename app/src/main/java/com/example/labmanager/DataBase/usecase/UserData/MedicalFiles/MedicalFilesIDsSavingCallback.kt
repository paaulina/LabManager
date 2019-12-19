package com.example.labmanager.DataBase.usecase.UserData.MedicalFiles

interface MedicalFilesIDsSavingCallback {
    fun onSaveSuccess(fileID: String)
    fun onFailure()
}