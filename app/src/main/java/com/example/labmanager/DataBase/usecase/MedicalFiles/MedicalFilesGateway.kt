package com.example.labmanager.DataBase.usecase.MedicalFiles

import com.example.labmanager.Model.MedicalFile

interface MedicalFilesGateway{
    fun getAllMedicalFiles(medicalFilesCallback: MedicalFilesCallback)
    fun saveMedicalFile(medicalFile: MedicalFile, medicalFilesSavingCallback: MedicalFilesSavingCallback)
    fun deleteMedicalFile(medicalFile: MedicalFile, medicalFilesSavingCallback: MedicalFilesSavingCallback)
}