package com.example.labmanager.dataBase.usecase.medicalFiles

import com.example.labmanager.model.MedicalFile

interface MedicalFilesGateway{
    fun getAllMedicalFiles(medicalFilesCallback: MedicalFilesCallback)
    fun saveMedicalFile(medicalFile: MedicalFile, medicalFilesSavingCallback: MedicalFilesSavingCallback)
    fun deleteMedicalFile(medicalFile: MedicalFile, medicalFilesSavingCallback: MedicalFilesSavingCallback)
}