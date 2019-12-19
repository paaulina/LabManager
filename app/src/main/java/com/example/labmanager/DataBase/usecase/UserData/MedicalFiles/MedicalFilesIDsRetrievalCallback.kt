package com.example.labmanager.DataBase.usecase.UserData.MedicalFiles

import com.example.labmanager.Model.MedicalFile
import java.util.ArrayList

interface MedicalFilesIDsRetrievalCallback {
    fun onRetrievalSuccess(emptyFilesList: ArrayList<MedicalFile>)
    fun onRetrievalFailure()
}