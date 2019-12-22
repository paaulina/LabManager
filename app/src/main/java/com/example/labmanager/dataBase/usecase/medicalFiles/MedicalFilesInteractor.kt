package com.example.labmanager.dataBase.usecase.medicalFiles

import com.example.labmanager.model.MedicalFile
import com.example.labmanager.service.MedicalFilesManager

class MedicalFilesInteractor (
    private val gateway: MedicalFilesGateway,
    private val filesPresenter: MedicalFilesPresenter?,
    private val savingPresenter: MedicalFilesSavingResultPresenter?
) : MedicalFilesCallback,
    MedicalFilesSavingCallback{

    fun retrieveMedicalFiles(){
        gateway.getAllMedicalFiles(this)
    }

    override fun onRetrievalSuccess(medicalFilesArray: ArrayList<MedicalFile>) {
        filesPresenter!!.presentMedicalFiles(MedicalFilesManager().sortFilesByName(medicalFilesArray))
    }

    override fun onRetrievalFailure() {
        filesPresenter!!.presentMedicalFilesRetrievalError()
    }

    fun saveMedicalFile(medicalFile: MedicalFile){
        gateway.saveMedicalFile(medicalFile, this)
    }

    fun deleteMedicalFile(medicalFile: MedicalFile){
        gateway.deleteMedicalFile(medicalFile, this)
    }

    override fun onSuccess() {
        savingPresenter!!.onSavingSuccess()
    }

    override fun onFailure() {
        savingPresenter!!.onSavingFailure()
    }

}