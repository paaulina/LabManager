package com.example.labmanager.dataBase.usecase.medicalFiles

import com.example.labmanager.model.MedicalFile

interface MedicalFilesPresenter {
    fun presentMedicalFiles(medicalFilesArrayList: ArrayList<MedicalFile>)
    fun presentMedicalFilesRetrievalError()
}