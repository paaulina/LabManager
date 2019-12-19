package com.example.labmanager.DataBase.usecase.MedicalFiles

import com.example.labmanager.Model.MedicalFile

class MedicalFilesInteractor (
    val gateway: MedicalFilesGateway,
    val filesPresenter: MedicalFilesPresenter?,
    val savingPresenter: MedicalFilesSavingResultPresenter?
) : MedicalFilesCallback,
    MedicalFilesSavingCallback{

    fun retrieveMedicalFiles(){
        gateway.getAllMedicalFiles(this)
    }

    override fun onRetrievalSuccess(medicalFilesArray: ArrayList<MedicalFile>) {
        filesPresenter!!.presentMedicalFiles(medicalFilesArray)
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