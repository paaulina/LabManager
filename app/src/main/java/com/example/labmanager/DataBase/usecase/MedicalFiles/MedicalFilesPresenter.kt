package com.example.labmanager.DataBase.usecase.MedicalFiles

import com.example.labmanager.Model.MedicalFile

interface MedicalFilesPresenter {
    fun presentMedicalFiles(medicalFilesArrayList: ArrayList<MedicalFile>)
    fun presentMedicalFilesRetrievalError()
}