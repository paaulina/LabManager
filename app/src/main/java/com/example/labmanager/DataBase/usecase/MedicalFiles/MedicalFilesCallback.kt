package com.example.labmanager.DataBase.usecase.MedicalFiles

import com.example.labmanager.Model.MedicalFile

interface MedicalFilesCallback {
    fun onRetrievalSuccess(medicalFilesArray: ArrayList<MedicalFile>)
    fun onRetrievalFailure()
}