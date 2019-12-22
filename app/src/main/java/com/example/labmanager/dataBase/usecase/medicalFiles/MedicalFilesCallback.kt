package com.example.labmanager.dataBase.usecase.medicalFiles

import com.example.labmanager.model.MedicalFile

interface MedicalFilesCallback {
    fun onRetrievalSuccess(medicalFilesArray: ArrayList<MedicalFile>)
    fun onRetrievalFailure()
}