package com.example.labmanager.dataBase.usecase.userData.MedicalFiles

interface MedicalFilesIDsSavingCallback {
    fun onSaveSuccess(fileID: String)
    fun onFailure()
}