package com.example.labmanager.dataBase.usecase.userData.MedicalFiles

import com.example.labmanager.model.MedicalFile
import java.util.ArrayList

interface MedicalFilesIDsRetrievalCallback {
    fun onRetrievalSuccess(emptyFilesList: ArrayList<MedicalFile>)
    fun onRetrievalFailure()
}